package com.coursework.autosalon.web.controller;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coursework.autosalon.web.cart.Cart;
import com.coursework.autosalon.web.cart.CartItem;
import com.coursework.autosalon.web.model.Car;
import com.coursework.autosalon.web.model.OrderForm;
import com.coursework.autosalon.web.model.PurchaseRequest;
import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.model.UserCartItem;
import com.coursework.autosalon.web.repository.CarRepository;
import com.coursework.autosalon.web.repository.PurchaseRequestRepository;
import com.coursework.autosalon.web.repository.UserCartItemRepository;
import com.coursework.autosalon.web.repository.UserRepository;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {

    private final CarRepository carRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;
    private final UserRepository userRepository;
    private final UserCartItemRepository userCartItemRepository;

    public CartController(CarRepository carRepository,
                          PurchaseRequestRepository purchaseRequestRepository,
                          UserRepository userRepository,
                          UserCartItemRepository userCartItemRepository) {
        this.carRepository = carRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
        this.userRepository = userRepository;
        this.userCartItemRepository = userCartItemRepository;
    }

    // ===== создание корзины в сессии + подъем из БД =====

    @ModelAttribute("cart")
    public Cart cart(Authentication authentication) {
        Cart cart = new Cart();

        if (authentication != null && authentication.getName() != null) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user -> {
                var savedItems = userCartItemRepository.findByUser(user);
                for (UserCartItem sci : savedItems) {
                    cart.addItem(sci.getCar());
                }
            });
        }

        return cart;
    }

    @GetMapping
    public String showCart(@ModelAttribute("cart") Cart cart,
                           Model model,
                           @ModelAttribute(name = "orderForm", binding = false) OrderForm orderForm) {

        if (orderForm == null) {
            orderForm = new OrderForm();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("orderForm", orderForm);
        return "cart";
    }

    @GetMapping("/order")
    public String showOrderForm(@ModelAttribute("cart") Cart cart,
                                Model model) {
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("orderForm", new OrderForm());
        return "order";
    }

    // ===== добавление в корзину =====

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable("id") Long id,
                            @ModelAttribute("cart") Cart cart,
                            RedirectAttributes redirectAttributes,
                            Authentication authentication) {

        Optional<Car> carOpt = carRepository.findById(id);
        if (carOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Болид не найден.");
            return "redirect:/cars";
        }

        Car car = carOpt.get();

        if (!Boolean.TRUE.equals(car.getAvailable())) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Этот болид уже недоступен для покупки."
            );
            return "redirect:/cars/" + id;
        }

        boolean alreadyInCart = cart.contains(id);
        cart.addItem(car);   // сама корзина не даст добавить дубликат

        // синхронизируем с БД, если пользователь авторизован
        if (!alreadyInCart && authentication != null && authentication.getName() != null) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user -> {
                if (!userCartItemRepository.existsByUserAndCarId(user, id)) {
                    UserCartItem entity = new UserCartItem();
                    entity.setUser(user);
                    entity.setCar(car);
                    userCartItemRepository.save(entity);
                }
            });
        }

        if (alreadyInCart) {
            redirectAttributes.addFlashAttribute(
                    "infoMessage",
                    "Этот болид уже был в корзине."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Болид добавлен в корзину."
            );
        }

        return "redirect:/cart";
    }

    // ===== удаление из корзины =====

    @PostMapping("/remove/{id}")
    @Transactional
    public String removeFromCart(@PathVariable("id") Long id,
                                 @ModelAttribute("cart") Cart cart,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {

        cart.removeItem(id);

        // удаляем из сохранённой корзины в БД
        if (authentication != null && authentication.getName() != null) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user ->
                    userCartItemRepository.deleteByUserAndCarId(user, id)
            );
        }

        redirectAttributes.addFlashAttribute(
                "infoMessage",
                "Болид удалён из корзины."
        );
        return "redirect:/cart";
    }

    // ===== оформление заявки =====

    @PostMapping("/order")
    @Transactional
    public String submitOrder(@ModelAttribute("cart") Cart cart,
                              OrderForm orderForm,
                              RedirectAttributes redirectAttributes,
                              SessionStatus sessionStatus,
                              Authentication authentication) {

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Корзина пуста, оформить заявку нельзя."
            );
            return "redirect:/cart";
        }

        if (authentication == null || authentication.getName() == null) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Не удалось определить текущего пользователя."
            );
            return "redirect:/cart";
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден: " + email));

        // 1-й проход: проверка доступности
        for (CartItem item : cart.getItems()) {
            if (item.getCar() == null || item.getCar().getId() == null) continue;

            Long carId = item.getCar().getId();
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new IllegalStateException("Болид не найден: id=" + carId));

            if (!Boolean.TRUE.equals(car.getAvailable())) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Болид " + car.getBrand() + " уже недоступен для покупки."
                );
                return "redirect:/cart";
            }
        }

        // 2-й проход: фиксация
        for (CartItem item : cart.getItems()) {
            if (item.getCar() == null || item.getCar().getId() == null) continue;

            Long carId = item.getCar().getId();
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new IllegalStateException("Болид не найден: id=" + carId));

            if (!Boolean.TRUE.equals(car.getAvailable())) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Болид " + car.getBrand() + " уже недоступен для покупки."
                );
                return "redirect:/cart";
            }

            car.setAvailable(false);
            carRepository.save(car);

            PurchaseRequest pr = new PurchaseRequest();
            pr.setUser(user);
            pr.setCar(car);
            pr.setCarBrandSnapshot(car.getBrand());
            pr.setPriceSnapshot(car.getPrice());

            purchaseRequestRepository.save(pr);
        }

        // очищаем корзину в БД и в сессии
        userCartItemRepository.deleteByUser(user);
        cart.clear();
        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Заявка отправлена. Болиды зарезервированы за вами."
        );

        return "redirect:/cart";
    }

    // ===== очистка корзины =====

    @PostMapping("/clear")
    @Transactional
    public String clearCart(@ModelAttribute("cart") Cart cart,
                            SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes,
                            Authentication authentication) {

        cart.clear();

        if (authentication != null && authentication.getName() != null) {
            String email = authentication.getName();
            userRepository.findByEmail(email).ifPresent(user ->
                    userCartItemRepository.deleteByUser(user)
            );
        }

        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("infoMessage", "Корзина очищена.");
        return "redirect:/cart";
    }
}

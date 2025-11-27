package es.daw.foodexpressmvc.controller;

import es.daw.foodexpressmvc.dto.RestaurantDTO;
import es.daw.foodexpressmvc.service.RestaurantsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantsService  restaurantsService;

    @GetMapping
    public String listRestaurants(Model model) {

        List<RestaurantDTO> restaurants = restaurantsService.getAllRestaurants();

        model.addAttribute("restaurants", restaurants);

        return "restaurants/restaurants";

    }

//    @GetMapping("/menu")
//    //public String showMenu(Principal principal, Model model) {
//    public String showMenu(){
//        //model.addAttribute(principal.getName());
//        return "restaurants/restaurants-menu";
//
//    }

    @GetMapping("/create")
    public String showForm(Model model, Principal principal) {
        model.addAttribute(principal.getName());
        model.addAttribute("restaurant", new RestaurantDTO());
        model.addAttribute("mode","create");
        return "restaurant-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("restaurant") RestaurantDTO restaurantDTO,
                         Model model, Principal principal) {

        RestaurantDTO saved = restaurantsService.create(restaurantDTO);
        model.addAttribute(saved);
        // Pendiente: enviar el restaurante salvado..
        return "restaurants/create-success"; //plantilla
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        // mostrar la lista de todos los restaurantes
        restaurantsService.delete(id);
        return "redirect:/restaurants"; //endpoint!!!!

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        // FORMA 1:
        // - restaurantsService.getAllRestaurants();
        // - filtro la lista vía java

        // FORMA 2:
        // - Webclient

        RestaurantDTO restDTO = restaurantsService.findById(id);

        model.addAttribute("mode","update");
        model.addAttribute("restaurant",restDTO);
        return "restaurants/restaurant-form";

    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("restaurant") RestaurantDTO restaurantDTO,
                         BindingResult bindingResult,
                         Model model
    ){

        if (bindingResult.hasErrors()){
            model.addAttribute("mode","update");
            return "restaurants/restaurant-form"; //pendiente leer!!!!
        }


        restaurantsService.update(id,restaurantDTO);


        return "redirect:/restaurants"; // patrong PRG - Post Redirect Get
    }



}

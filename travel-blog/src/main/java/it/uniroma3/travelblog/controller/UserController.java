package it.uniroma3.travelblog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.travelblog.model.User;
import it.uniroma3.travelblog.service.UserService;

@Controller
public class UserController {

	@Autowired private UserService userService;
	
	
	/* Form per l'aggiunta di un nuovo user
	 * Aggiungo al model un nuovo user nel quale 
	 * memorizzero' i valori ricevuti dalla form
	 */
	@GetMapping("/addUserForm")
	public String addUserForm(Model model) {
		model.addAttribute(new User());
		return null;
	}
	
	/* Aggiunta di un user al DB
	 * -Verifico che i dati ottenuti dalla form siano validi
	 * 		- se si, aggiungo l'user al DB e torno alla home
	 * 		-altrimenti ritorno alla form mostrando le violazioni
	 */
	@PostMapping("/addUser")
	public String addUser(@Valid @ModelAttribute("user") User user, 
								BindingResult bindingResult, Model model) {
	
		// validation(?) TBD
		
		if(!bindingResult.hasErrors()) {
			userService.save(user);
			model.addAttribute("user", user);
			return null; // ritorna alla form di inserimento dati
		}
		return null; // ritorna alla home page ?
	}
	

	/* Visualizzazione profilo di un determinato user
	 * - Cerco l'user nel DB 
	 * 		- se e' presente --> mostra il profilo
	 * 		- altrimenti mostro una pagina di errore ? (TBD)
	 */
	@GetMapping("/user/{id}")
	public String getUser(@Valid @PathVariable("id") Long id, Model model ) {
		User user = this.userService.findById(id);
		if(user != null) {
			model.addAttribute(user);
			return null; // return alla pagina che mostra un profilo user
		}
		return null; // return a una pagina di errore ?
	}
	
	
	
}

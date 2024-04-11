
/**
 * This class contains custom validation rules. 
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
//public class MyDslValidator extends AbstractMyDslValidator {
//	
//
//}

package org.xtext.example.mydsl.validation;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.CheckType;
import org.xtext.example.mydsl.myDsl.Game;
import org.xtext.example.mydsl.myDsl.CourseType;
import org.xtext.example.mydsl.myDsl.Dish;
import org.xtext.example.mydsl.myDsl.MyDslPackage;
import org.xtext.example.mydsl.myDsl.Participant;
import org.xtext.example.mydsl.myDsl.PotluckParty;
import org.eclipse.xtext.validation.ValidationMessageAcceptor;


public class Validator extends AbstractMyDslValidator {
//public class MyDslValidator extends AbstractDeclarativeValidator { // don't use this one! it makes the runtime editor fail 

    // Rule1: The number of guests cannot exceed the host family's venue capacity
	  @Check
	  public void checkMaxParticipants(PotluckParty party) {
	    int maxParticipants = 15;
	    if (party.getParticipants().size() > maxParticipants) {
	      error("The number of participants (" + party.getParticipants().size() + ") should be less than or equal to " + maxParticipants + ".",
	            MyDslPackage.Literals.POTLUCK_PARTY__PARTICIPANTS);
	    }
	  }
	  
	   // Rule2: dish number should be match with guest number 
	  @Check
	  public void checkSufficientMainDishes(PotluckParty party) {
	    long mainDishes = party.getCourses().stream()
	                           .filter(course -> "Main".equals(course.getType()))
	                           .flatMap(course -> course.getDishes().stream())
	                           .count();

	    if (mainDishes < party.getParticipants().size()) {
	      error("The number of dishes should be greater than or equal to the number of participants (" + party.getParticipants().size() + ").",
	            MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
	    }
	  }
	  
	  
    // Rule3: A party at least have one course 
    @Check(CheckType.NORMAL)
    public void checkPotluckParty(PotluckParty party) { 
        if (party.getCourses().isEmpty()) {
            error("A PotluckParty must have at least one course", MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
        }
    }

    // Rule4: Every game must have a host - But this has been expressed in the metamodel itself!
    @Check(CheckType.NORMAL)
    public void checkGame(Game game) {
       
        if (game.getHost() == null) {
            error("Every game must have a host", MyDslPackage.Literals.GAME__HOST);
        }
    }
    

	  // Rule5: need to provide special dietary meal
	  @Check
	  public void checkVeganDishRequirement(PotluckParty party) {
	    boolean veganRequired = party.getParticipants().stream()
	                                .anyMatch(p -> "Vegan".equals(p.getDietaryCategory()));

	    boolean veganDishAvailable = party.getCourses().stream()
	                                      .filter(course -> "Main".equals(course.getType()))
	                                      .flatMap(course -> course.getDishes().stream())
	                                      .anyMatch(dish -> "Vegan".equals(dish.getDietaryCategory()));

	    if (veganRequired && !veganDishAvailable) {
	      error("There are guests with a 'vegan' dietary requirement, at least one main dish must be 'Vegan'.",
	             MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
	          
	    }
	  }

	  // Rule6: need to provide special dietary meal (with providing guests' list accordingtly)
	  @Check
	  public void checkVeganDishRequirementWithName(PotluckParty party) {
	      // Find guests with 'vegan' dietary requirement
	      List<String> veganGuests = party.getParticipants().stream()
	                                      .filter(p -> "Vegan".equals(p.getDietaryCategory()))
	                                      .map(Participant::getName)
	                                      .collect(Collectors.toList());

	      boolean veganDishAvailable = party.getCourses().stream()
	                                        .filter(course -> "Main".equals(course.getType()))
	                                        .flatMap(course -> course.getDishes().stream())
	                                        .anyMatch(dish -> "Vegan".equals(dish.getDietaryCategory()));
	      
	      

	      if (veganGuests.isEmpty() && !veganDishAvailable) {
	          String guestNames = String.join(", ", veganGuests);
	          error("There are guests with a 'vegan' dietary requirement (" + guestNames + "), at least one main dish must be 'Vegan'.",
	                MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
	      }
	  

	      // If there are vegan guests but no vegan dish available
	      if (!veganGuests.isEmpty() && !veganDishAvailable) {
	          String guestNames = String.join(", ", veganGuests);
	          error("There are guests with a 'vegan' dietary requirement (" + guestNames + "), at least one main dish must be 'Vegan'.",
	                MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
	      }
	  }

		// Rule 7: at least three appetizers provided
		  public static final String INSUFFICIENT_APPETIZER_DISHES = "insufficientAppetizerDishes";
	
		  @Check
		  public void checkAppetizerDishes(PotluckParty party) {
		      long appetizerDishesCount = party.getCourses().stream()
		                                       .filter(course -> CourseType.APPETIZER == course.getType())
		                                       .flatMap(course -> course.getDishes().stream())
		                                       .count();
	
		      if (appetizerDishesCount < 3) {
		          error("There must be at least three appetizer dishes.", 
		                MyDslPackage.Literals.POTLUCK_PARTY__COURSES, 
		                INSUFFICIENT_APPETIZER_DISHES);
		      }
		  }

	  
	      // Rule 8:at least two desset provided
	      public static final String INSUFFICIENT_DESSERT_DISHES = "insufficientDessertDishes";

	      @Check
	      public void checkDessertDishes(PotluckParty party) {
	          long dessertDishesCount = party.getCourses().stream()
	                                         .filter(course -> CourseType.DESSERT == course.getType())
	                                         .flatMap(course -> course.getDishes().stream())
	                                         .count();

	          if (dessertDishesCount < 2) {
	        	  
	        	//  warning(String message, EObject object, EStructuralFeature feature, String code, String... issueData)

	              error ("There must be at least two dessert dishes.", MyDslPackage.Literals.POTLUCK_PARTY__COURSES);
	          
	          }
	      }
	  }

	 

package net.codejava.springmvc;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.codejava.springmvc.model.HotelReview;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/reviews")
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String home(Locale locale, Model model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addAttribute("serverTime", formattedDate );
//		
//		return "home";
//	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showReviewForm(Map<String, Object> model) {
		System.out.println("First");
		HotelReview hotelReview = new HotelReview();
		model.put("hotelReview", hotelReview);
		//model.addAttribute("reviewsForm", hr);
		//...
		return "reviewsPage";
	}
	@RequestMapping(method = RequestMethod.POST)
	public String getReviewForm(@ModelAttribute("hotelReview") HotelReview hotelReview, Map<String, Object> model) throws InterruptedException, IOException, KeeperException {

		int hotelid = hotelReview.getHotelId();
		String rev = hotelReview.getReview();
		System.out.println("Hotel id: "+ hotelid);
		System.out.println("Review is: "+ rev);
		
		KafkaProducer.run(hotelid, rev);
		return "home";
	}
	
}

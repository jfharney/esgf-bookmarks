package org.esgf.bookmarks;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BookmarkController {

	
	@RequestMapping(method=RequestMethod.GET, value="/bookmark")
    public @ResponseBody String getBookmark(HttpServletRequest request) {
		System.out.println("In getBookmark");
		String response = "<bookmark>bookmark</bookmark>";
	
		return response;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/bookmark")
	public @ResponseBody void addBookmark(@RequestBody String body, HttpServletRequest request) {
		System.out.println("In addBookmark");
		
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/bookmark")
	public @ResponseBody void removeBookmark(HttpServletRequest request) {
		System.out.println("In removeBookmark");
		
	}
	
	
}

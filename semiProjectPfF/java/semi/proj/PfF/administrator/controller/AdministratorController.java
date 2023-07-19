package semi.proj.PfF.administrator.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import semi.proj.PfF.administrator.model.service.AdministratorService;
import semi.proj.PfF.administrator.model.vo.Picture;
import semi.proj.PfF.common.Pagination;
import semi.proj.PfF.common.model.vo.PageInfo;
import semi.proj.PfF.member.model.vo.Member;
import semi.proj.PfF.order.model.vo.OrderProduct;
import semi.proj.PfF.product.model.vo.Product;

@Controller
public class AdministratorController {
	
	@Autowired
	AdministratorService aService;
	
	@RequestMapping("main.ad")
	public String main() {
		return "checkPayment";
	}
	
	@RequestMapping("statistics.ad")
	public String statistics() {
		return "statistics";
	}
	
	// 寃곗젣�옄/寃곗젣 �닔 �넻怨�
		@RequestMapping("numOfPay.ad")
		public void numOfPay(HttpServletResponse response) {
//			1. 寃곗젣�옄 �닔 , �궇吏쒕줈 洹몃９諛붿씠 �빐�꽌 COUNT �븯�뒗�뜲 媛숈��궗�엺�씠 寃곗젣�뻼�쓣�븣 以묐났�씠�릺硫댁븞�맖 
//			2. 寃곗젣 �닔 , �궇吏쒕줈 洹몃９諛붿씠�빐�꽌 COUNT �빐�빞�븯吏��븡�쓣源�
//			3. �궇吏쒕뱾
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			Date oneMonthAgo = calendar.getTime();
			
			ArrayList<Integer> numPayer	= aService.selectNumPayer(oneMonthAgo);
			ArrayList<Integer> numPay = aService.selectNumPay(oneMonthAgo);
			ArrayList<Date> date = aService.selectDate(oneMonthAgo);

			response.setContentType("application/json; charset=UTF-8");
			
			GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
			Gson gson = gb.create();
			
			Map<String, Object> responseData = new HashMap<>();
		    responseData.put("numPayer", numPayer);
		    responseData.put("numPay", numPay);
		    responseData.put("date", date);
			
			try {
				gson.toJson(responseData, response.getWriter());
			} catch (JsonIOException | IOException e) {
				e.printStackTrace();
			}
			
		}
		
		// �씪蹂꾧껐�젣湲덉븸 �넻怨�
		@RequestMapping("amountOfPay.ad")
		public void amountOfPay(HttpServletResponse response) {
//			1. �씪蹂� 寃곗젣 湲덉븸 , �궇吏쒕줈 洹몃９諛붿씠�빐�꽌 sum
//			2. �빐�떦�궇吏� �룷�븿�븳 7�씪媛꾪룊洹� 湲덉븸
//			3. �궇吏쒕뱾
			
			ArrayList<Integer> sumPrice = aService.selectSumPrice();
			ArrayList<Integer> avgPrice = aService.selectAvgPrice();
			ArrayList<Date> date = aService.selectAmountPayDate();
			
			response.setContentType("application/json; charset=UTF-8");
			
			GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
			Gson gson = gb.create();
			
			Map<String, Object> responseData = new HashMap<>();
			responseData.put("sumPrice", sumPrice);
		    responseData.put("avgPrice", avgPrice);
		    responseData.put("date", date);
		    
		    try {
				gson.toJson(responseData, response.getWriter());
			} catch (JsonIOException | IOException e) {
				e.printStackTrace();
			}
			
		}
	
	@GetMapping("checkPayment.ad")
	public String paymentView() {
		return "checkPayment";
	}
	
	@GetMapping("searchOrder.ad")
	public String searchOrder(@RequestParam(value="page", required=false) Integer page, Model model, @RequestParam("name") String name, @RequestParam("address") String address, 
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("payMethod") String payMethod) {
		int currentPage = 1;
		if(page != null) {
			currentPage = page;
		}
		
		HashMap<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("name", name);
		searchMap.put("address", address);
		searchMap.put("startDate", startDate);
		searchMap.put("endDate", endDate);
		searchMap.put("payMethod", payMethod);
		
		int orderCount = aService.getOrderCount(searchMap);
//		System.out.println(orderCount);
		if(orderCount != 0) {
			PageInfo pi = Pagination.getPageInfo(currentPage, orderCount, 10);
			ArrayList<Integer> orders = aService.searchAllOrder(pi, searchMap);
//			System.out.println(pi);
//			System.out.println(orders);
			
			ArrayList<OrderProduct> list = aService.selectAllOrderProduct(orders);
//			System.out.println(list);
			model.addAttribute("orders", orders);
			model.addAttribute("list", list);
			model.addAttribute("pi", pi);
		}
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("name", name);
		model.addAttribute("address", address);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("payMethod", payMethod);
		return "checkPayment";
	}
	
	@RequestMapping(value = "/mng_member.ad", method = RequestMethod.GET)
	public String memberList(HttpSession Session, Model model) {
		ArrayList<Member> list = aService.memberList();
		model.addAttribute("memberList", list);
		return "mng_member";
	}
	
	
	@PostMapping(value="/updateMember.ad")
	public String updateMember(@ModelAttribute Member m, Model model) {
		int result = aService.updateMember(m);
		System.out.println(result);
		
		return "redirect:/mng_member.ad";
	}
	
	@PostMapping("/deleteMember.ad")
	public String deleteMember(@RequestParam("memberId") String memberId, @RequestParam("memberStatus") String memberStatus) {
		String status = memberStatus.equals("Y") ? "N" : "Y";
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("memberId", memberId);
		map.put("memberStatus", status);
		
	    aService.deleteMember(map);
	    return "redirect:/mng_member.ad";
	}
	
//	@PostMapping(value="/insertProduct.pr")//�긽�뭹�닔�젙 
//	public String AdminproductInsert(Product p) {
//		aService.AdminproductInsert(p);
//		return "redirect:/productList2.pr";
//	}
//	
	@PostMapping("insertProduct.pr")
	public  String insertinto(MultipartFile [] file, Product p, HttpServletRequest request) {
		
        String fileurl = "D:\\WorkSpace\\semi\\semiProjectPfF\\src\\main\\webapp\\resources\\product_img\\";
        
        for (int i = 0; i < file.length; i++) {
        	//사용자가 업로드한 파일이 있다면 아래 로직 수행
            String filerealname = file[i].getOriginalFilename(); // 원본 파일 명
            long filesSize = file[i].getSize(); // 파일 사이즈
            
            
            //저장할 때 파일 이름이 겹치지 않도록 랜덤 id를 부여한다.
            UUID uuid = UUID.randomUUID();
            
            String filename = uuid+filerealname;
            String safefile = fileurl+uuid+filerealname;
           
            System.out.println(filename);
            if(i == 0) {
            	 p.setProductMainPic(filename);
            }else if( i == 1) {
            	p.setProductDetailPic1(filename);
            }else if( i ==2) {
            	p.setProductDetailPic2(filename);
            }else if( i == 3) {
            	p.setProductDetailPic3(filename);
            }
            try {
				file[i].transferTo(new File(safefile));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    
       int result = aService.insertProduct(p);
       System.out.println(p.getProductMainPic());

        return "redirect:/productList2.pr";
	}
	
	@GetMapping("/delete.pr") 
	public String deletProduct(@RequestParam("productNo")  String productNo) {
		aService.deleteProduct(productNo);
		return "redirect:/productList2.pr";
	}
	
}
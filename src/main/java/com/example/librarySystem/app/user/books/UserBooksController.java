package com.example.librarySystem.app.user.books;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.librarySystem.domain.model.Books;
import com.example.librarySystem.domain.model.ColBooks;
import com.example.librarySystem.domain.model.Genre;
import com.example.librarySystem.domain.model.Lending;
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.LendingService;
import com.example.librarySystem.domain.service.PublisherService;
import com.example.librarySystem.domain.service.ReserveService;
import com.example.librarySystem.domain.service.SuperUserDetails;
import com.example.librarySystem.validator.lending.LendValidator;

/**
 * 
 * UserBooksControllerクラス
 * 
 * 利用者 書籍くらす
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class UserBooksController {
	
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	ColBooksService colBooksService;
	
	@Autowired
	LendingService lendingService;
	
	@Autowired
	ReserveService reserveService;
	
	@Autowired
	LendValidator lendValidator;
	
	//form-backing bean初期化
	@ModelAttribute("lendForm")
	public LendForm setLendForm() {
		return new LendForm();
	}
	
	//バリエーションを追加
	@InitBinder("lendForm")
	public void initBinderForLnedForm(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(lendValidator);
	}
	
	//form-backing bean初期化
	@ModelAttribute("serchBooksForm")
	public SearchBooksForm setSearchBooksForm() {
		return new SearchBooksForm();
	}
	
	//未入力項目をnullにする
	@InitBinder("searchBooksForm")
	public void initBinderForuserSerchBooksForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * 
	 * booksメソッド
	 * 
	 * 書籍検索
	 * 
	 * @param searchBooksForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping("user/books")
	public String books(@Validated SearchBooksForm searchBooksForm ,BindingResult bindingResult,Model model) {
		
		//ジャンルリスト、出版社リストを取得しビューへ渡す
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);

		//バリデーションエラー時、全書籍データを取得しビューへ渡す
		if(bindingResult.hasErrors()) {
			List<Books> bookslist = booksService.readAll();
			
			model.addAttribute("bookslist", bookslist);
			return "user/books/books";
		}
		
		List<Books> bookslist = booksService.searchBooks(searchBooksForm);
		
		model.addAttribute("bookslist", bookslist);
				
		return "user/books/books";
	}
	
	/**
	 * 
	 * bookdetailメソッド
	 * 
	 * 書籍詳細
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/user/books/{id}")
	public String bookdetail(@PathVariable Integer id, Model model) {
		
		//指定番号の書籍情報を取得
		Books book = booksService.readByBooksId(id);
		
		//指定書籍の貸出最大日数を計算し、貸出可能か設定
		long period = reserveService.searchMaxReservePeriod(id, LocalDate.now() ,-1L);
		if(colBooksService.findLendCheck(id) && period > 0 ) {
			model.addAttribute("check", "true");
		}else {
			model.addAttribute("check", "false");
		}
		
		model.addAttribute("period", period);
		model.addAttribute("book", book);
		
		return "/user/books/bookdetail";
		
	}
	
	/**
	 * 
	 * booklendメソッド
	 * 
	 * 書籍貸出
	 * 
	 * @param bookId
	 * @param lendForm
	 * @param model
	 * @return
	 */
	@RequestMapping("user/books/lend")
	public String booklend(@RequestParam Integer bookId,LendForm lendForm, Model model) {
		
		//書籍情報をフォームへセット
		lendForm.setBookId(bookId);
		lendForm.setReserveDate(LocalDate.now());					//貸出日を本日に設定
		lendForm.setMinReturnDate(LocalDate.now().plusDays(1));		//最短返却日を翌日に設定
		lendForm.setScheduledReturnDate(LocalDate.now().plusDays(1));	//暫定返却日を翌日に設定
		lendForm.setMaxperiod(reserveService.searchMaxReservePeriod(bookId, LocalDate.now(), reserveService.NON_PESERVE_ID) );	//最長返却期間を計算し設定
		lendForm.setMaxReturnDate(LocalDate.now().plusDays(lendForm.getMaxperiod()));	//最長返却予定日を設定
		
		return "user/books/lend";
	}
	
	/**
	 * 
	 * lendconfメソッド
	 * 
	 * 書籍貸出確定
	 * 
	 * @param lendForm
	 * @param br
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("user/books/lendconf")
	public String lendconf(@Validated LendForm lendForm, BindingResult br,Model model,RedirectAttributes redirectAttributes) {
		
		//バリデーションエラー時、前のページへ戻る
		if(br.hasErrors()) {
			return "user/books/lend";
		}
		
		//貸出処理
		String userId = ((SuperUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUserId(); //ユーザーIDを取得
		Long colBooksId = colBooksService.readLendColBooksId(lendForm.getBookId()); //貸し出す蔵書IDを取得
		
		//貸出情報をインスタンス化、取得しデータベースへ登録
		Lending lending = new Lending(userId, colBooksId,LocalDateTime.now().minusSeconds(1), lendForm.getScheduledReturnDate());
		lending=lendingService.saveLend(lending);
		
		//蔵書情報を獲得し、蔵書状況を貸出中に更新
		ColBooks colBooks = colBooksService.readColBooksId(lending.getColBooksId());
		colBooks.setSituationName(SituationName.LENDING);
		colBooksService.saveColBooks(colBooks);
		
		//登録したlending情報を渡しリダイレクト
		redirectAttributes.addAttribute("lending", lending);
		
		return "redirect:/user/books/lendingconf";
		
	}
	
	/**
	 * 
	 * lendingconfメソッド
	 * 
	 * 貸出内容の確認
	 * 
	 * @param lending
	 * @param model
	 * @return
	 */
	@RequestMapping("user/books/lendingconf")
	public String ledingconf(@RequestParam Lending lending,Model model) {
		Books books = booksService.readByBooksId(lending.getColBooks().getBooksId());
		model.addAttribute("lending", lending);
		model.addAttribute("books", books);
		return "user/books/lendingconf";
	}
	
}

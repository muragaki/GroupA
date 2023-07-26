package com.example.librarySystem.app.admin.books;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.example.librarySystem.domain.model.Publisher;
import com.example.librarySystem.domain.model.SearchBooksForm;
import com.example.librarySystem.domain.model.SituationName;
import com.example.librarySystem.domain.service.BooksService;
import com.example.librarySystem.domain.service.ColBooksService;
import com.example.librarySystem.domain.service.GenreService;
import com.example.librarySystem.domain.service.PublisherService;


/**
 * 
 * AdminBooksControllerクラス
 * 
 * 管理者書籍コントローラー
 * 
 * @author 中尾 寿晃
 *
 */
@Controller
public class AdminBooksController {
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	GenreService genreService;
	
	@Autowired
	PublisherService publisherService;
	
	@Autowired
	ColBooksService colBooksService;
	
	
	//form-backing bean初期化
	
	@ModelAttribute("adminBooksForm")
	public AdminBooksForm setAdminBookForm() {
		return new AdminBooksForm();
	}
	
	@ModelAttribute("colBooksForm")
	public ColBooksForm setColBookForm() {
		return new ColBooksForm();
	}
	
	@ModelAttribute("searchBooksForm")
	public SearchBooksForm setSearchBooksForm() {
		return new SearchBooksForm();
	}
	
	//未入力フィールドをnullに設定
	@InitBinder("searchBooksForm")
	public void initBinderForuserSerchBooksForm(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	/**
	 * booksメソッド
	 * 
	 * 管理者書籍一覧
	 * 
	 * @param searchBooksForm
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/books")
	public String books(@Validated SearchBooksForm searchBooksForm ,BindingResult bindingResult, Model model) {
		
		//ジャンル、出版社のリストを取得、ビューへ渡す
		List<Publisher> publisherlist = publisherService.readSearchAll();
		List<Genre> genrelist = genreService.readSearchAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		//バリデーションエラー時、全書籍データを取得しビューへ
		if(bindingResult.hasErrors()) {
			
			List<Books> bookslist = booksService.readAll();
			model.addAttribute("bookslist", bookslist);

			return "admin/books/books";
		}
		
		//検索条件から書籍を検索しリストで取得、ビューへ渡す
		List<Books> bookslist = booksService.searchBooks(searchBooksForm);
		model.addAttribute("bookslist", bookslist);
		
		return "admin/books/books";
	}
	
	/**
	 * newbooksメソッド
	 * 
	 * 書籍登録準備
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("admin/books/newbooks")
	public String newbooks(Model model) {
		
		List<Genre> genrelist = genreService.readAll();
		List<Publisher> publisherlist = publisherService.readAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		
		return "admin/books/newbooks";
	}
	
	/**
	 * booksAddメソッド
	 * 
	 * 書籍登録
	 * 
	 * @param adminBooksForm
	 * @param br
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("admin/books/booksave")
	public String booksAdd(@Validated AdminBooksForm adminBooksForm, BindingResult br, Model model, RedirectAttributes redirectAttributes) {
		
		//バリデーションエラー時、前のページに戻る
		if(br.hasErrors()) {
			
			List<Genre> genrelist = genreService.readAll();
			List<Publisher> publisherlist = publisherService.readAll();
			
			model.addAttribute("genrelist", genrelist);
			model.addAttribute("publisherlist", publisherlist);
			return "admin/books/newbooks";
		}
		
		//入力内容をインスタンス化し、データベースへ登録
		Books book = new Books(adminBooksForm.getBookId(), adminBooksForm.getTitle(), adminBooksForm.getAuthor() , adminBooksForm.getReleaseDate()
							  ,adminBooksForm.getGenreId(), null, adminBooksForm.getPublisherId(), null, adminBooksForm.getOverview());
		
		book = booksService.saveBook(book);
		
		//登録した主キーIDを渡してリダイレクト
		redirectAttributes.addAttribute("id", book.getBookId());
		
		return "redirect:/admin/books/booksconf/{id}";
	}
	
	/**
	 * booksconfメソッド
	 * 
	 * 登録内容の確認
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("admin/books/booksconf/{id}")
	public String booksconf(@PathVariable Integer id ,Model model) {
		Books book = booksService.readByBooksId(id);
		model.addAttribute("book", book);
		return "admin/books/bookconf";
	}
	
	/**
	 * editメソッド
	 * 
	 * 書籍情報編集
	 * 
	 * @param id
	 * @param adminBooksForm
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/books/edit/{id}")
	public String edit(@PathVariable Integer id,AdminBooksForm adminBooksForm,Model model) {
		Books book = booksService.readByBooksId(id);
		
		adminBooksForm.setAdminBooksForm(book);
		
		List<Genre> genrelist = genreService.readSearchAll();
		List<Publisher> publisherlist = publisherService.readSearchAll();
		
		model.addAttribute("genrelist", genrelist);
		model.addAttribute("publisherlist", publisherlist);
		return "admin/books/bookedit";
		
	}
	
	/**
	 * addColBooksメソッド
	 * 
	 * 蔵書の確認、追加
	 * 
	 * @param id
	 * @param colBooksForm
	 * @param model
	 * @return
	 */
	@RequestMapping("admin/books/addcolbook/{id}")
	public String addColBooks(@PathVariable Integer id,ColBooksForm colBooksForm, Model model) {
		
		//該当書籍の蔵書リストの取得
		List<ColBooks> colBooksList = colBooksService.findBookId(id);
		
		//書籍idと現在時刻をフォームに追加
		colBooksForm.setBookId(id);
		colBooksForm.setRegistrationDate(LocalDate.now());
		
		//蔵書の状況リストを取得
		List<SituationName> situationList = Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING,SituationName.NOT_AVAILABLE);
		
		model.addAttribute("colBooksList", colBooksList);
		model.addAttribute("situationlist", situationList);
		
		return "admin/books/colbooks";
		
	}
	
	/**
	 * saveColBooksメソッド
	 * 
	 * 蔵書登録
	 * 
	 * @param colBooksForm
	 * @param br
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("admin/books/colbooksadd")
	public String saveColBooks(@Validated ColBooksForm colBooksForm ,BindingResult br,Model model,RedirectAttributes redirectAttributes) {

		//バリデーションエラー時前のページに戻る
		if(br.hasErrors()) {
			List<ColBooks> colBooksList = colBooksService.findBookId(colBooksForm.getBookId());
			List<SituationName> situationList = Arrays.asList(SituationName.AVAILABLE,SituationName.LENDING,SituationName.NOT_AVAILABLE);
			
			model.addAttribute("colBooksList", colBooksList);
			model.addAttribute("situationlist", situationList);
			return "admin/books/colbooks"; 
		}
		
		//蔵書登録後、登録IDを渡し、リダイレクト
		colBooksService.addColBooks(colBooksForm.getBookId(), colBooksForm.getRegistrationDate());
		
		redirectAttributes.addAttribute("id",colBooksForm.getBookId());
		
		return "redirect:/admin/books/addcolbook/{id}";
	}
	
	/**
	 * updateSituationメソッド
	 * 
	 * 蔵書の状況更新
	 * 
	 * @param colBooksId
	 * @param situationName
	 * @param redirectAttributes
	 * @return
	 */
	@PostMapping("admin/books/updatesituation")
	public String updateSituation(@RequestParam Long colBooksId,@RequestParam SituationName situationName, RedirectAttributes redirectAttributes) {
		
		//更新する蔵書を獲得、書き換えた後に上書き
		ColBooks colbooks = colBooksService.readColBooksId(colBooksId);
		colbooks.setSituationName(situationName);
		
		colBooksService.saveColBooks(colbooks);
		redirectAttributes.addAttribute("id", colbooks.getBooksId());
		
		return "redirect:/admin/books/addcolbook/{id}";
	}
	
}

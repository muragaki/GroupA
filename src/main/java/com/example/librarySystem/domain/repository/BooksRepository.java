package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.librarySystem.domain.model.Books;


/**
 * BooksRepositoryクラス
 * 
 * エンティティ型：Books
 * 主キー型：Integer
 * 
 * @author 3030673
 *
 */
public interface BooksRepository extends JpaRepository<Books, Integer> {

	/**
	 * ブックIDの昇降順
	 * @return
	 */
	public List<Books> findAllByOrderByBookIdAsc();
	
	
	/**
	 * 書籍検索リスト
	 * @param genrename
	 * @param title
	 * @param author
	 * @param fromdate
	 * @param todate
	 * @param publishername
	 * @param overview
	 * @return List Books
	 */
	@Query("SELECT book FROM Books book "
			+ "JOIN Genre genre ON book.genreId = genre.genreId " 						//ジャンルデータ結合
			+ "JOIN Publisher publisher ON book.publisherId = publisher.publisherId "	//出版社データ結合
			
			+ "WHERE book.genre.genreName LIKE :genrename "								//ジャンル検索
			+ "AND book.title LIKE %:title% "											//書籍名検索
			+ "AND book.author LIKE %:author% "											//著者検索
			+ "AND book.publisher.publisherName LIKE :publishername "					//出版社検索
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "						//発刊日範囲検索
			+ "AND book.overview LIKE %:overview% "										//概要検索
			
			+ "ORDER BY book.bookId ")
	public List<Books> findSearch(@Param("genrename") String genrename,@Param("title") String title, 
								  @Param("author") String author,@Param("fromdate") LocalDate fromdate,
								  @Param("todate") LocalDate todate,@Param("publishername") String publishername,
								  @Param("overview") String overview);
	

}

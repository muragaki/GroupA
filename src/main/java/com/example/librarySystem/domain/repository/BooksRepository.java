package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.librarySystem.domain.model.Books;

public interface BooksRepository extends JpaRepository<Books, Integer> {

	public List<Books> findAllByOrderByBookIdAsc();
	
	@Query("SELECT book FROM Books book "
			+ "JOIN Genre genre ON book.genreId = genre.genreId "
			+ "JOIN Publisher publisher ON book.publisherId = publisher.publisherId "
			+ "WHERE book.genre.genreName LIKE %:genrename% "
			+ "AND book.title LIKE %:title% "
			+ "AND book.author LIKE %:author% "
			+ "AND book.publisher.publisherName LIKE %:publishername%"
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "
			+ "AND book.overview LIKE %:overview%")
	public List<Books> findSearch(@Param("genrename") String genrename,@Param("title") String title, 
								  @Param("author") String author,@Param("fromdate") LocalDate fromdate,
								  @Param("todate") LocalDate todate,@Param("publishername") String publishername,
								  @Param("overview") String overview);

}

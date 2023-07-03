package com.example.librarySystem.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.librarySystem.domain.model.Books;

public interface BooksRepository extends JpaRepository<Books, Integer> {

	public List<Books> findAllByOrderByBookId();
	
	@Query("SELECT book FROM Books book "
			+ "WHERE book.title LIKE %:title% "
			+ "AND book.author LIKE %:author% "
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "
			+ "AND book.overview LIKE %:overview%")
	public List<Books> findSearch(@Param("title") String title, @Param("author") String author,
								  @Param("fromdate") LocalDate fromdate,@Param("todate") LocalDate todate,
								  @Param("overview") String overview);
	
	@Query("SELECT book FROM Books book "
			+ "WHERE book.genreId = :genreId "
			+ "AND book.title LIKE %:title% "
			+ "AND book.author LIKE %:author% "
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "
			+ "AND book.overview LIKE %:overview%")
	public List<Books> findSearchGenre(@Param("genreId") Integer genreId, @Param("title") String title, @Param("author") String author,
									   @Param("fromdate") LocalDate fromdate,@Param("todate") LocalDate todate,
									   @Param("overview") String overview);
	
	@Query("SELECT book FROM Books book "
			+ "WHERE book.title LIKE %:title% "
			+ "AND book.author LIKE %:author% "
			+ "AND book.publisherId = :publisherId "
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "
			+ "AND book.overview LIKE %:overview%")
	public List<Books> findSearchPublisher(@Param("title") String title, @Param("author") String author,@Param("publisherId") Integer publisherId,
										   @Param("fromdate") LocalDate fromdate,@Param("todate") LocalDate todate,
										   @Param("overview") String overview);
	
	@Query("SELECT book FROM Books book "
			+ "WHERE book.genreId = :genreId "
			+ "AND book.title LIKE %:title% "
			+ "AND book.author LIKE %:author% "
			+ "AND book.publisherId = :publisherId "
			+ "AND book.releaseDate BETWEEN :fromdate AND :todate "
			+ "AND book.overview LIKE %:overview%")
	public List<Books> findSearchGenreAndPublisher(@Param("genreId") Integer genreId,@Param("title") String title, @Param("author") String author,
												   @Param("publisherId") Integer publisherId, @Param("fromdate") LocalDate fromdate,@Param("todate") LocalDate todate,
												   @Param("overview") String overview);
}

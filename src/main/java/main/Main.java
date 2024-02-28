package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entities.Author;
import entities.Book;
import entities.Editorial;
import entities.Library;

public class Main {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("LibraryJPA");
		EntityManager em = factory.createEntityManager();
		
		System.out.println("Conexión completada!");
		
		//Dar de alta 3 autores
		Author author1 = new Author("Gabriel","García Márquez", new Date(27,2,6));
		Author author2 = new Author("Agatha","Christie", new Date(90,8,15));
		Author author3 = new Author("Stephen","King", new Date(47,8,21));
		//Dar de alta 2 editoriles
		Editorial editorial1 = new Editorial("Penguin Random House", "123 Main Street, Cityville");
		Editorial editorial2 = new Editorial("HarperCollins", "456 Oak Avenue, Townsville");
		//Dar de alta 8 libros
		//1:1-2:2-3:6-4:3-5:7-6:4-7:8-8:5
		Book book1 = new Book("Cien años de soledad", 25.99, null, null);
		Book book2 = new Book("El asesinato de Roger Ackroyd", 19.99, null, null);
		Book book3 = new Book("IT", 21.50, null, null);
		Book book4 = new Book("El otoño del patriarca", 29.95, null, null);
		Book book5 = new Book("Asesinato en Mesopotamia", 22.75, null, null);
		Book book6 = new Book("The shining", 18.99, null, null);
		Book book7 = new Book("Crónica de una muerte anunciada", 20.50, null, null);
		Book book8 = new Book("Asesinato en el Orient Express", 24.75, null, null);
		//Dar de alta 2 librerías con 4 libros cada una de ellas
		Library library1 = new Library("Librería La Pluma Dorada", "Ana Martínez", "789 Oak Street, Citytown");
		Library library2 = new Library("Leer es Vivir", "Sara Pérez", "404 Spruce Avenue, Hamletton");
		
		//Listas de elementos auxiliares
		List<Book> listAuthor1Books = new ArrayList<Book>();
		listAuthor1Books.add(book1);
		listAuthor1Books.add(book3);
		listAuthor1Books.add(book5);		
		
		List<Book> listAuthor2Books = new ArrayList<Book>();
		listAuthor2Books.add(book2);
		listAuthor2Books.add(book4);
		listAuthor2Books.add(book6);
		listAuthor2Books.add(book8);		
		
		List<Book> listAuthor3Books = new ArrayList<Book>();
		listAuthor3Books.add(book7);		
		
		List<Book> listEditorial1Books = new ArrayList<Book>();
		listEditorial1Books.add(book1);
		listEditorial1Books.add(book2);
		listEditorial1Books.add(book3);
		listEditorial1Books.add(book4);		
		
		List<Book> listEditorial2Books = new ArrayList<Book>();
		listEditorial2Books.add(book5);
		listEditorial2Books.add(book6);
		listEditorial2Books.add(book7);
		listEditorial2Books.add(book8);		
		
		//Orden de aparción ids libros:
		//1:1-2:2-3:6-4:3-5:7-6:4-7:8-8:5
		//creación: 1-2-3-4-5-6-7-8
		//bbdd:     1-2-6-3-7-4-8-5		
		//Creando relaciones - AuthorLibros		
		CreateLinkAuthorBooks(author1, listAuthor1Books); //1-1:6:7 = (ok)
		CreateLinkAuthorBooks(author2, listAuthor2Books); //2-2:3:4:5 = (ok)
		CreateLinkAuthorBooks(author3, listAuthor3Books); //3-8 = (ok)
		//Creando relaciones - EditorialBooks
		CreateLinkEditorialBooks(editorial1, listEditorial1Books); //1-1:2:6:3 = ok
		CreateLinkEditorialBooks(editorial2, listEditorial2Books); //2-7:4:8:5 = ok
		//Creando relaciones - LibrarysBooks
		CreateLinkLibraryBook(library1, book1); //1-1 = ok
		CreateLinkLibraryBook(library1, book2); //1-2 = ok
		CreateLinkLibraryBook(library1, book3); //1-6 = ok
		CreateLinkLibraryBook(library1, book4);	//1-3 = ok
		
		CreateLinkLibraryBook(library2, book2); //2-2 = ok
		CreateLinkLibraryBook(library2, book4); //2-3 = ok
		CreateLinkLibraryBook(library2, book6); //2-4 = ok
		CreateLinkLibraryBook(library2, book8); //2-5 = ok
		
		//Persistencias
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		/*
		em.persist(book1);
		em.persist(book2);
		em.persist(book3);
		em.persist(book4);
		em.persist(book5);
		em.persist(book6);
		em.persist(book7);
		em.persist(book8);
		*/
		//Si tiene cascada no habría que opner libros pero la persistencia se ve agrupada en función
		//de la lista añadida si pones los autores antes o después de los libros. Muy interesante
		em.persist(author1);
		em.persist(author2);
		em.persist(author3);
		
		em.persist(editorial1);
		em.persist(editorial2);		
		
		em.persist(library1);
		em.persist(library2);
		
		et.commit();
		
		System.out.println("Enlaces completados!");
		
		
		//JorgeOperations(em);
		
		//CONSULTAS
		//Listado de libros
		
		System.out.println("\nxxxxxxxxxxx LISTADO DE LIBROS xxxxxxxxxxx");
		List<Book> listBooks = em.createQuery("from Book b").getResultList();
		for(Book b : listBooks) {
			System.out.println(b);
		}
		
		
		//Listado de autores
		System.out.println("\nxxxxxxxxxxx LISTADO DE AUTORES xxxxxxxxxxx");
		List<Author> listAuthors = em.createQuery("from Author a").getResultList();
		for(Author a : listAuthors) {		
			System.out.println(a);
			for (Book b : a.getBooks()) {
				System.out.println("     " + b);
			}
		}
		
		
		//Listado de librerías
		System.out.println("\nxxxxxxxxxxx LISTADO DE LIBRERÍAS xxxxxxxxxxx");
		List<Library> listLibraries = em.createQuery("from Library l").getResultList();
		for(Library l : listLibraries) {
			System.out.println(l.toString());			
			for (Book b : l.getListBooks()) {
				System.out.println("     " + b);
			}
		}
		
		
		//Listado de libros y su librería
		System.out.println("\nxxxxxxxxxxx LISTADO DE LIBROS Y SUS LIBRERÍAS xxxxxxxxxxx");
		List<Object[]> resultList = em.createQuery("SELECT b, l FROM Book b JOIN b.listLibraries l", Object[].class).getResultList();
		for (Object[] result : resultList) {
		    Book book = (Book) result[0];
		    Library library = (Library) result[1];

		    System.out.println("Libro: " + book.getTitle());
		    System.out.println("Librería: " + library.getName());
		    System.out.println("---");
		}
		
		System.out.println("\nxxxxxxxxxxx FIN DE CONSULTAS xxxxxxxxxxx");
		
		em.close();
	}
	
	private static void CreateLinkAuthorBooks(Author author, List<Book> books) {
		author.setBooks(books);	
		for(int i = 0; i < books.size(); i++) {
			books.get(i).setAuthor(author);
		}
	}
	
	private static void CreateLinkEditorialBooks(Editorial editorial, List<Book> books) {
		editorial.setListBooks(books);
		for(int i = 0; i < books.size(); i++) {
			books.get(i).setEditorial(editorial);
		}
	}
	
	private static void CreateLinkLibraryBook(Library library, Book book) {		
		library.getListBooks().add(book);
		book.getListLibraries().add(library);
	}
	
	public static void JorgeOperations(EntityManager em) {
		//OPERACIONES
				//Dar de alta 3 autores
				Author author1 = new Author("Gabriel","García Márquez", new Date(27,2,6));
				Author author2 = new Author("Agatha","Christie", new Date(90,8,15));
				Author author3 = new Author("Stephen","King", new Date(47,8,21));
				
				EntityTransaction et = em.getTransaction();
				et.begin();
				em.persist(author1);
				em.persist(author2);
				em.persist(author3);
				
				//Dar de alta 2 editoriles
				Editorial editorial1 = new Editorial("Penguin Random House", "123 Main Street, Cityville");
				Editorial editorial2 = new Editorial("HarperCollins", "456 Oak Avenue, Townsville");
				em.persist(editorial1);
				em.persist(editorial2);
				
				//Dar de alta 8 libros
				Book book1 = new Book("Cien años de soledad", 25.99, author1, editorial1);
				Book book2 = new Book("El asesinato de Roger Ackroyd", 19.99, author2, editorial1);
				Book book3 = new Book("IT", 21.50, author1, editorial1);
				Book book4 = new Book("El otoño del patriarca", 29.95, author2, editorial1);
				Book book5 = new Book("Asesinato en Mesopotamia", 22.75, author1, editorial2);
				Book book6 = new Book("The shining", 18.99, author2, editorial2);
				Book book7 = new Book("Crónica de una muerte anunciada", 20.50, author3, editorial2);
				Book book8 = new Book("Asesinato en el Orient Express", 24.75, author2, editorial2);
				
				List<Book> listAuthor1Books = new ArrayList<Book>();
				listAuthor1Books.add(book1);
				listAuthor1Books.add(book3);
				listAuthor1Books.add(book5);
				author1.setBooks(listAuthor1Books);
				
				List<Book> listAuthor2Books = new ArrayList<Book>();
				listAuthor2Books.add(book2);
				listAuthor2Books.add(book4);
				listAuthor2Books.add(book6);
				listAuthor2Books.add(book8);
				author2.setBooks(listAuthor2Books);
				
				List<Book> listAuthor3Books = new ArrayList<Book>();
				listAuthor3Books.add(book7);
				author3.setBooks(listAuthor3Books);
				
				List<Book> listEditorial1Books = new ArrayList<Book>();
				listEditorial1Books.add(book1);
				listEditorial1Books.add(book2);
				listEditorial1Books.add(book3);
				listEditorial1Books.add(book4);
				editorial1.setListBooks(listEditorial1Books);
				
				List<Book> listEditorial2Books = new ArrayList<Book>();
				listEditorial2Books.add(book5);
				listEditorial2Books.add(book6);
				listEditorial2Books.add(book7);
				listEditorial2Books.add(book8);
				editorial2.setListBooks(listEditorial2Books);
				
				
				em.persist(author1);
				em.persist(author2);
				em.persist(author3);
				
				em.persist(editorial1);
				em.persist(editorial2);
				
				em.persist(book1);
				em.persist(book2);
				em.persist(book3);
				em.persist(book4);
				em.persist(book5);
				em.persist(book6);
				em.persist(book7);
				em.persist(book8);
				
				//Dar de alta 2 librerías con 4 libros cada una de ellas
				Library library1 = new Library("Librería La Pluma Dorada", "Ana Martínez", "789 Oak Street, Citytown");
				List<Book> listBooks1 = new ArrayList<Book>();
				listBooks1.add(book1);
				listBooks1.add(book2);
				listBooks1.add(book3);
				listBooks1.add(book4);
				library1.setListBooks(listBooks1);
				Library library2 = new Library("Leer es Vivir", "Sara Pérez", "404 Spruce Avenue, Hamletton");
				List<Book> listBooks2 = new ArrayList<Book>();
				listBooks2.add(book5);
				listBooks2.add(book6);
				listBooks2.add(book7);
				listBooks2.add(book8);
				library2.setListBooks(listBooks2);
				em.persist(library1);
				em.persist(library2);
				
				et.commit();
				
				//CONSULTAS
				//Listado de libros
				System.out.println("\nxxxxxxxxxxx LISTADO DE LIBROS xxxxxxxxxxx");
				List<Book> listBooks = em.createQuery("from Book b").getResultList();
				for(Book b : listBooks) {
					System.out.println(b);
				}
				
				//Listado de autores
				System.out.println("\nxxxxxxxxxxx LISTADO DE AUTORES xxxxxxxxxxx");
				List<Author> listAuthors = em.createQuery("from Author a").getResultList();
				for(Author a : listAuthors) {
					System.out.println(a);
				}
				
				//Listado de librerías
				System.out.println("\nxxxxxxxxxxx LISTADO DE LIBRERÍAS xxxxxxxxxxx");
				List<Library> listLibraries = em.createQuery("from Library l").getResultList();
				for(Library l : listLibraries) {
					System.out.println("Librería: " + l.getName() + "\n" + "Libros: " + l.getListBooks());
					System.out.println("---");
				}
				
				//Listado de libros y su librería
				System.out.println("\nxxxxxxxxxxx LISTADO DE LIBROS Y SUS LIBRERÍAS xxxxxxxxxxx");
				List<Object[]> resultList = em.createQuery("SELECT b, l FROM Book b JOIN b.listLibraries l", Object[].class).getResultList();
				for (Object[] result : resultList) {
				    Book book = (Book) result[0];
				    Library library = (Library) result[1];

				    System.out.println("Libro: " + book.getTitle());
				    System.out.println("Librería: " + library.getName());
				    System.out.println("---");
				}
				em.close();
				System.out.println("\nxxxxxxxxxxx FIN DE CONSULTAS xxxxxxxxxxx");
	}

}

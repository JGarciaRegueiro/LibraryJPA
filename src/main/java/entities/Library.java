package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="librerias")
public class Library {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="nombre")
	private String name;
	
	@Column(name="dueño")
	private String owner;
	
	@Column(name="direccion")
	private String address;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="libreriaslibros",
			  joinColumns = {@JoinColumn(name="id_libreria", referencedColumnName = "id")},
			  inverseJoinColumns = {@JoinColumn(name="id_libro", referencedColumnName ="id")})
	private List<Book> listBooks;

	public Library() {
		super();
	}

	public Library(String name, String owner, String address) {
		super();
		this.name = name;
		this.owner = owner;
		this.address = address;
		listBooks = new ArrayList<Book>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Book> getListBooks() {
		return listBooks;
	}

	public void setListBooks(List<Book> listBooks) {
		this.listBooks = listBooks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Library))
			return false;
		Library other = (Library) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Library [id=" + id + ", name=" + name + ", owner=" + owner + ", address=" + address + ", listBooks="
				+ listBooks + "]";
	}

	
}


package com.Bookstore.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id",nullable = false,updatable = false)
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	@Column(name="email",nullable = false,updatable = false)
	private String email;
	private String phone;
	private boolean enabled=true;
 
	/*
	 * private String currentPassword;
	 * private String txtNewPassword;
	 * private String txtConfirmPassword;
	 */
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private ShoppingCart ShoppingCart;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserShipping> userShippingList;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserPayment> userPaymentList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> orderList;
    
    

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	

	/*
	 * public String getTxtNewPassword()
	 * {
	 * return txtNewPassword;
	 * }
	 * 
	 * public void setTxtNewPassword(String txtNewPassword)
	 * {
	 * this.txtNewPassword = txtNewPassword;
	 * }
	 * 
	 * public String getTxtConfirmPassword()
	 * {
	 * return txtConfirmPassword;
	 * }
	 * 
	 * public void setTxtConfirmPassword(String txtConfirmPassword)
	 * {
	 * this.txtConfirmPassword = txtConfirmPassword;
	 * }
	 * 
	 * 
	 * public String getCurrentPassword()
	 * {
	 * return currentPassword;
	 * }
	 * 
	 * public void setCurrentPassword(String currentPassword)
	 * {
	 * this.currentPassword = currentPassword;
	 * }
	 * 
	 */
	public List<UserShipping> getUserShippingList()
	{
		return userShippingList;
	}

	public void setUserShippingList(List<UserShipping> userShippingList)
	{
		this.userShippingList = userShippingList;
	}

	public List<UserPayment> getUserPaymentList()
	{
		return userPaymentList;
	}

	public void setUserPaymentList(List<UserPayment> userPaymentList)
	{
		this.userPaymentList = userPaymentList;
	}

	public ShoppingCart getShoppingCart()
	{
		return ShoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart)
	{
		ShoppingCart = shoppingCart;
	}

	public List<Order> getOrderList()
	{
		return orderList;
	}

	public void setOrderList(List<Order> orderList)
	{
		this.orderList = orderList;
	}


   
}

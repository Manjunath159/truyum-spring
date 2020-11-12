package com.cognizant.truyum.dao;

import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import com.cognizant.truyum.model.Cart;
import com.cognizant.truyum.model.MenuItem;

@Component
@ImportResource("classpath:spring-config.xml")

public class CartDaoCollectionImpl implements CartDao {
	@Autowired

	private HashMap<Long, Cart> userCarts;

	public HashMap<Long, Cart> getUserCarts() {
		return userCarts;
	}

	public void setUserCarts(HashMap<Long, Cart> userCarts) {
		this.userCarts = userCarts;
	}

	public void addCartItem(long userId, long menuItemId) throws ParseException, IOException, NullPointerException {
		// TODO Auto-generated method stub
		MenuItemDao menuItemDao = new MenuItemDaoCollectionImpl();
		MenuItem item = menuItemDao.getMenuItem(menuItemId);

		if (userCarts.containsKey(userId)) {
			List<MenuItem> menuItemList = userCarts.get(userId).getMenuItemList();
			menuItemList.add(item);
			userCarts.get(userId).setMenuItemList(menuItemList);
		} else {
			List<MenuItem> newObj = new ArrayList<>();
			newObj.add(item);
			Cart obj1 = new Cart(newObj);
			userCarts.put(userId, obj1);
		}

	}

	@Override
	public List<MenuItem> getAllCartItems(long userId) throws CartEmptyException {
		// TODO Auto-generated method stub
		Cart obj = userCarts.get(userId);
		List<MenuItem> lst = obj.getMenuItemList();
		if (lst.isEmpty()) {
			throw new CartEmptyException();
		} else {
			double total = 0;
			for (MenuItem item : lst) {
				total += item.getPrice();
			}
			obj.setTotal(total);
		}
		return lst;
	}

	public void removeCartItem(long userId, long menuItemId) {
		// TODO Auto-generated method stub
		Cart cart = userCarts.get(userId);
		List<MenuItem> lst = cart.getMenuItemList();
		for (MenuItem obj : lst) {
			if (obj.getId() == menuItemId) {
				lst.remove(obj);
			}

		}
	}
}

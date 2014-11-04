package com.cisoft.lazyorder.bean.goods;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lecion on 10/28/14.
 */
public class GoodsCart {

    private static GoodsCart cart;


    /**
     * 商品对应的数量
     */
    private HashMap<Integer, Integer> goodsCount;

    /**
     * 购物车中的商品列表
     */
    private HashMap<Integer, Goods> goodsList;

    private GoodsCart() {
        goodsList = new HashMap<Integer, Goods>();
        goodsCount = new HashMap<Integer, Integer>();
    }

    public static GoodsCart getInstance() {
        if (cart == null) {
            synchronized (GoodsCart.class) {
                cart = new GoodsCart();
            }
        }
        return cart;
    }

    /**
     * 添加商品
     * @param g
     */
    public void addGoods(Goods g) {
        //不存在商品则添加，存在则数目自增
        if (!isContains(g)) {
            goodsList.put(g.getId(), g);
            goodsCount.put(g.getId(), 1);
        } else {
            int count = goodsCount.get(g.getId());
            goodsCount.put(g.getId(), ++count);
        }
    }

    /**
     * 添加指定数量的商品
     * @param g
     * @param num
     */
    public void addGoods(Goods g, int num) {
        //不存在商品则添加，存在则数目自增
        if (!isContains(g)) {
            goodsList.put(g.getId(), g);
            goodsCount.put(g.getId(), num);
        } else {
            int count = goodsCount.get(g.getId());
            goodsCount.put(g.getId(), count + num);
        }
    }

    /**
     * 获取商品
     * @param id
     * @return
     */
    public Goods getGoods(Integer id) {
        return goodsList.get(id);
    }


    public Goods getGoods(Goods g) {
        return getGoods(g.getId());
    }

    /**
     * 删除商品
     * @param id
     * @return
     */
    public void delGoods(Integer id) {
        if (goodsList.containsKey(id)) {
            goodsCount.put(id, goodsCount.get(id) - 1);
        }
        if (goodsCount.get(id) <= 0) {
            goodsList.remove(id);
        }
    }

    public void delGoods(Goods g) {
        delGoods(g.getId());
    }

    /**
     * 是否存在商品
     * @param goods
     * @return
     */
    public boolean isContains(Goods goods) {
        return goodsList.containsKey(goods.getId()) || goodsList.containsValue(goods);
    }

    public boolean isContains(int id) {
        return isContains(Integer.valueOf(id));
    }

    /**
     * 获取指定商品的数量
     * @param id
     * @return
     */
    public int getGoodsCount(Integer id) {
        return goodsCount.get(id);
    }

    public int getGoodsCount(Goods goods) {
        return getGoodsCount(goods.getId());
    }

    /**
     * 获取所有商品的数量
     * @return
     */
    public int getTotalCount() {
        int total = 0;
        Iterator iterator = goodsCount.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Integer count = (Integer) entry.getValue();
            total += count;
        }
        return total;
    }


    /**
     * 获取指定商品总价
     * @return
     */
    public double getGoodsPrice(int id) {
        return getGoodsCount(id) * getGoods(id).getCmPrice();
    }

    /**
     * 获取所有商品总价
     * @return
     */
    public double getTotalPrice() {
        double price = 0;
        Iterator iterator = goodsList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            int count = getGoodsCount((Integer) entry.getKey());
            Goods g = (Goods) entry.getValue();
            price += count * g.getCmPrice();
        }
        return price;
    }

    /**
     * 清空购物车
     */
    public void clear() {
        goodsList.clear();
        goodsCount.clear();
    }


}

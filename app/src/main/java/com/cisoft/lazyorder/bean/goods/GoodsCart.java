package com.cisoft.lazyorder.bean.goods;

import java.util.ArrayList;
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
//    private HashMap<Integer, Integer> goodsCount;

    /**
     * 购物车中的商品列表
     */
    private HashMap<Integer, Goods> goodsList;

    private GoodsCart() {
        goodsList = new HashMap<Integer, Goods>();
//        goodsCount = new HashMap<Integer, Integer>();
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
     * 判断是否是同一家商店
     * @param shopId
     * @return
     */
    public boolean isSameShop(int shopId) {
        int id = getShopId();
        if (id == 0) {
            return true;
        } else {
            return shopId == id;
        }
    }

    /**
     * 取商家Id
     */
    public int getShopId() {
        int shopId = 0;
        if (getTotalCount() == 0) {
            shopId =  0;
        } else {
            Iterator iterator = goodsList.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Goods g = (Goods) entry.getValue();
                shopId = g.getShopId();
            }
        }
        return shopId;
    }

    /**
     * 添加商品
     * @param g
     */
    public void addGoods(Goods g) {
        //不存在商品则添加，存在则数目自增
        if (!isContains(g)) {
            g.setOrderNum(1);
            goodsList.put(g.getId(), g);
//            goodsCount.put(g.getId(), 1);
        } else {
            g.setOrderNum(g.getOrderNum() + 1);
            goodsList.put(g.getId(), g);
//            int count = goodsCount.get(g.getId());
//            goodsCount.put(g.getId(), ++count);
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
            g.setOrderNum(num);
            goodsList.put(g.getId(), g);
//            goodsCount.put(g.getId(), num);
        } else {
//            int count = goodsCount.get(g.getId());
//            goodsCount.put(g.getId(), count + num);
            g.setOrderNum(g.getOrderNum() + num);
            goodsList.put(g.getId(), g);
        }
    }

    /**
     * 根据Id给商品指定数量
     * @param id
     * @param num
     */
    public void setGoodsOrderNum(int id, int num) {
        if (!isContains(id) || num < 1) {
            return;
        }
        Goods g = goodsList.get(id);
        g.setOrderNum(num);
        goodsList.put(id, g);
    }

    /**
     * 根据Id获取商品
     * @param id
     * @return
     */
    public Goods getGoods(Integer id) {
        return goodsList.get(id);
    }

    /**
     * 获取商品
     * @param g
     * @return
     */
    public Goods getGoods(Goods g) {
        return getGoods(g.getId());
    }

    /**
     * 根据Id减少商品数量
     * @param id
     * @return
     */
    public void decGoods(Integer id) {
        if (isContains(id)) {
            Goods g = goodsList.get(id);
            int count = g.getOrderNum();
            if (count > 1) {
                g.setOrderNum(count - 1);
                goodsList.put(id, g);
            }
            if (count <= 1) {
                goodsList.remove(id);
            }
        }

    }

    /**
     * 减少商品数量
     * @param g
     */
    public void decGoods(Goods g) {
        decGoods(g.getId());
    }

    /**
     * 删除同类商品
     * @param g
     */
    public void delGoods(Goods g) {
        delGoods(g.getId());
    }

    /**
     * 根据Id删除同类商品
     * @param id
     */
    public void delGoods(Integer id) {
        if (isContains(id)) {
            this.goodsList.remove(id);
        }
    }


    /**
     * 获取指定商品的数量
     * @param id
     * @return
     */
    public int getGoodsCount(Integer id) {
        return goodsList.get(id).getOrderNum();
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
        Iterator iterator = goodsList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Goods g = (Goods) entry.getValue();
            total += g.getOrderNum();
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
//            int count = getGoodsCount((Integer) entry.getKey());
            Goods g = (Goods) entry.getValue();
            price += g.getOrderNum() * g.getCmPrice();
        }
        return price;
    }

    /**
     * 获取所有商品
     * @return
     */
    public ArrayList<Goods> getAllGoods() {
        ArrayList<Goods> goods = new ArrayList<Goods>();
        Iterator iterator = goodsList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Goods g = (Goods) entry.getValue();
            goods.add(g);
        }
        return goods;
    }

    /**
     * 清空购物车
     */
    public void clear() {
        goodsList.clear();
    }

    /**
     * 是否存在商品
     * @param goods
     * @return
     */
    public boolean isContains(Goods goods) {
        return goodsList.containsValue(goods);
    }

    /**
     * 是否存在商品
     * @param id
     * @return
     */
    public boolean isContains(int id) {
        return goodsList.containsKey(id);
    }


}

use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize,PartialEq, Eq, Clone)]
pub struct Stock {
    batchNo: String,
    qty: String,
    price: f64,
    cost: f64,
    supplier: String,
    date: String,
}

#[derive(Serialize, Deserialize, PartialEq, Eq, Clone)]
pub struct Item {
    itemName: String,
    itemId: i32,
    totalQty: String,
    stocks: Vec<Stock>,
}

#[derive(Serialize, Deserialize)]
pub struct Items {
    items: Vec<Item>,
}

impl Stock {
    pub fn new( qty: String, price: f64, cost: f64, supplier: String, date: String) -> Self {
        Stock {
            batchNo: format!("{}_{}_{}", supplier, date, cost),
            qty,
            price,
            cost,
            supplier,
            date,
        }
    }
    
    pub fn get_batchNo(&self) -> &String {
        &self.batchNo
    }

    pub fn get_qty(&self) -> &String {
        &self.qty
    }

    pub fn get_price(&self) -> &f64 {
        &self.price
    }

    pub fn get_cost(&self) -> &f64 {
        &self.cost
    }

    pub fn get_supplier(&self) -> &String {
        &self.supplier
    }

    pub fn get_date(&self) -> &String {
        &self.date
    }

    pub fn set_qty(&mut self, qty: &String) -> &String {
        self.qty = qty.clone();
        &self.qty
    }
    pub fn set_price(&mut self, price: f64) -> &f64 {
        self.price = price;
        &self.price
    }
    pub fn set_cost(&mut self, cost: f64) -> &f64 {
        self.cost = cost;
        &self.cost
    }
    pub fn set_supplier(&mut self, supplier: &String) -> &String {
        self.supplier = supplier.clone();
        &self.supplier
    }
    pub fn set_date(&mut self, date: &String) -> &String {
        self.date = date.clone();
        &self.date
    }
    pub fn to_string(&self) -> String {
        format!("Stock {{ batchNo: {}, qty: {}, price: {}, cost: {}, supplier: {}, date: {} }}", self.batchNo, self.qty, self.price, self.cost, self.supplier, self.date)
    }
}

impl Item {
    pub fn new(itemName: String, itemId: i32, totalQty: String, stocks: Vec<Stock>) -> Self {
        Item {
            itemName,
            itemId,
            totalQty,
            stocks,
        }
    }

    pub fn get_itemName(&self) -> &String {
        &self.itemName
    }

    pub fn get_itemId(&self) -> &i32 {
        &self.itemId
    }

    pub fn get_totalQty(&self) -> &String {
        &self.totalQty
    }

    pub fn get_stocks(&self) -> &Vec<Stock> {
        &self.stocks
    }

    pub fn set_itemName(&mut self, itemName: &String) -> &String {
        self.itemName = itemName.clone();
        &self.itemName
    }

    pub fn set_itemId(&mut self, itemId: i32) -> &i32 {
        self.itemId = itemId;
        &self.itemId
    }

    pub fn set_totalQty(&mut self, totalQty: &String) -> &String {
        self.totalQty = totalQty.clone();
        &self.totalQty
    }

    pub fn set_stocks(&mut self, stocks: Vec<Stock>) -> &Vec<Stock> {
        self.stocks = stocks;
        &self.stocks
    }

    pub fn add_stock(&mut self, stock: Stock) {
        self.stocks.push(stock);
    }

    pub fn remove_stock(&mut self, batchNo: &String) {
        self.stocks.retain(|stock| &stock.batchNo != batchNo);
    }

    pub fn take_qty($mut self, qty: i32, batchNo: String) -> bool{
        for stock in $mut self.stocks.iter_mut() {
            if stock.batchNo != batchNo {
                continue;
            }
            let stock_qty: i32 = stock.qty.parse().unwrap();
            if stock_qty < qty {
                return false;
            }
            stock.qty = (stock_qty - qty).to_string();
            self.totalQty = (self.totalQty.parse::<i32>().unwrap() - qty).to_string();
            return true;
        }
    }

    pub fn to_string(&self) -> String {
        format!("Item {{ itemName: {}, itemId: {}, totalQty: {}, stocks: {:?} }}", self.itemName, self.itemId, self.totalQty, self.stocks)
    }
}

impl Items {
    pub fn new(items: Vec<Item>) -> Self {
        Items { items }
    }

    pub fn get_items(&self) -> &Vec<Item> {
        &self.items
    }

    pub fn add_item(&mut self, item: Item) {
        self.items.push(item);
    }

    pub fn remove_item(&mut self, itemId: i32) {
        self.items.retain(|item| item.itemId != itemId);
    }
}
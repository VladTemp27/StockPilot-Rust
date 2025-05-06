use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Clone, PartialEq, Eq)]
pub struct Supplier {
    name: String,
}

#[derive(Serialize, Deserialize)]
pub struct Suppliers {
    suppliers: Vec<Supplier>,
}

impl Supplier {
    pub fn new(name: String) -> Self {
        Supplier { name }
    }

    pub fn get_name(&self) -> &String {
        &self.name
    }
}

impl Suppliers {
    pub fn new(suppliers: Vec<Supplier>) -> Self {
        Suppliers { suppliers }
    }
    
    pub fn get_suppliers(&self) -> &Vec<Supplier> {
        &self.suppliers
    }

    pub fn add_supplier(&mut self, supplier: Supplier) {
        self.suppliers.push(supplier);
    }
    
    pub fn remove_supplier(&mut self, name: &String) {
        self.suppliers.retain(|supplier| &supplier.name != name);
    }
}
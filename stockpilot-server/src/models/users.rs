use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Clone, PartialEq,Eq)]
pub struct User {
    username: String,
    password: String,
    role: String,
}

#[derive(Serialize, Deserialize)]
pub struct Users {
    users: Vec<User>,
}

impl User{
    pub fn new(username: String, password: String, role: String) -> Self {
        User {
            username,
            password,
            role,
        }
    }
    
    pub fn get_username(&self) -> &String {
        &self.username
    }

    pub fn get_password(&self) -> &String {
        &self.password
    }

    pub fn get_role(&self) -> &String {
        &self.role
    }

    pub fn set_username(&mut self, username: &String) -> &String{
        self.username = username;
        &self.username
    }

    pub fn set_password(&mut self, password: String) -> &String{
        self.password = password;
        &self.password
    }

    pub fn set_role(&mut self, role: &String) -> &String{
        self.role = role;
        &self.role
    }

    pub fn to_string(&self) -> String {
        format!("User {{ username: {}, password: {}, role: {} }}", self.username, self.password, self.role)
    }
}

impl Users {
    pub fn new(users: Vec<User>) -> Self {
        Users { users }
    }
    
    pub fn get_users(&self) -> &Vec<User> {
        &self.users
    }

    pub fn add_user(&mut self, user: User) {
        self.users.push(user);
    }

    pub fn get_user_by_username(&self, username: &str) -> Option<&User> {
        self.users.iter().find(|user| user.get_username() == username)
    }
}
package com.mit.user.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admin")
public class Admin extends User{
}

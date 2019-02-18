package com.learning.data.generator

object Config{

    //TODO("Read from Properties")
    //TODO("Apply Decryption logic to db password")

    val dbHost = "localhost"
    val dbPort = "5432"
    val dbSchema = "LocalDB"
    val dbUser = "postgres"
    val dbConnUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbSchema?user=$dbUser"
    val dbDriver = "org.postgresql.Driver"

}
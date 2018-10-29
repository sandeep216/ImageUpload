package com.sandeepsingh.imageupload.feature.login.pojos

import java.io.Serializable

/**
 * Created by Sandeep on 10/22/18.
 */
class UserInfo(): Serializable {
    private var id: String? = null
    private var email: String = ""
    private var firstName: String? = null
    private var lastName: String = ""
    private var gender: String = ""
    private var provider: String? = null
    private var profilePic: String? = null
    private var accessToken: String? = null
    private var arrayOfInteractions : ArrayList<String> ?= null

    /**
     * Used for social logins
     * @param id
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     * @param provider
     */
    constructor(id: String, email: String, firstName: String, lastName: String, gender: String, provider: String) : this(){
        this.id = id
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.gender = gender
        this.provider = provider
    }

    /**
     * For Facebook
     * @param id
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     * @param provider
     * @param accessToken
     */
    constructor(id: String, email: String, firstName: String, lastName: String, gender: String, provider: String, accessToken: String?, arrayOfInteractions: ArrayList<String>, profilePic: String) : this() {
        this.id = id
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
        this.gender = gender
        this.provider = provider
        this.accessToken = accessToken
        this.arrayOfInteractions = arrayOfInteractions
        this.profilePic = profilePic
        //        lastUpdate = TimeConversions.getCurrentTime(TimeConversions.Zone.GMT, "dd-MM-yyyy HH:mm:ss.SSS");
    }

    constructor(provider: String) :this(){
        this.provider = provider
    }

    fun getId(): String? {
        return id
    }

    fun getEmail(): String {
        return email
    }

    fun getFirstName(): String? {
        return firstName
    }

    fun getLastName(): String {
        return lastName
    }

    fun getGender(): String {
        return gender
    }

    fun getProvider(): String? {
        return provider
    }

    fun getProfilePic(): String? {
        return profilePic
    }

    fun setProfilePic(profilePic: String) {
        this.profilePic = profilePic
    }

    fun getAccessToken(): String? {
        return accessToken
    }

    fun setId(id : String){
        this.id = id
    }

    fun setArrayOfInteractions(arrayOfInteractions : ArrayList<String>){
        this.arrayOfInteractions = arrayOfInteractions
    }

    fun getArrayOfInteraction() : ArrayList<String>{
        return arrayOfInteractions!!
    }

    override fun toString(): String {
        return "UserInfo{" +
                "accessToken='" + accessToken + '\''.toString() +
                ", id='" + id + '\''.toString() +
                ", email='" + email + '\''.toString() +
                ", firstName='" + firstName + '\''.toString() +
                ", lastName='" + lastName + '\''.toString() +
                ", gender='" + gender + '\''.toString() +
                ", provider='" + provider + '\''.toString() +
                ", profilePic='" + profilePic + '\''.toString() +
                '}'.toString()
    }
}
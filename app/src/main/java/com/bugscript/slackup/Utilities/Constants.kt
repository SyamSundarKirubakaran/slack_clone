package com.bugscript.slackup.Utilities

const val BASE_URL_CONSTANT = "https://slack-up.herokuapp.com/v1/"
const val SOCKET_URL = "https://slack-up.herokuapp.com/"
const val URL_REGISTER = "${BASE_URL_CONSTANT}account/register"
const val URL_LOGIN = "${BASE_URL_CONSTANT}account/login"
const val URL_CREATE_USER = "${BASE_URL_CONSTANT}user/add"
const val URL_GET_USER = "${BASE_URL_CONSTANT}user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL_CONSTANT}channel"
const val URL_GET_MESSAGES = "${BASE_URL_CONSTANT}message/byChannel/"


// Broadcast constant
const val BROADCAST_USER_DATA_CHNAGE = "BROADCAST_USER_DATA_CHNAGE"
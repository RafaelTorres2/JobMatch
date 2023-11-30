package com.example.aplicaciontfg2.utilidades;

import com.example.aplicaciontfg2.MainActivity;

import java.util.HashMap;

public class Constants {

    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_COLLECION_USERS = "cuentas";
    public static final String KEY_COLLECTION_COMPANIES = "empresas";
    public static final String KEY_NAME = "usuario";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PREFERENCE_NAME = "chatAppPrefence";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = MainActivity.firebaseAuth.getCurrentUser().getUid();
    public static final String KEY_USER_ID2 = "userId";
    public static final String KEY_PASSWORD = "contraseña";
    public static final String KEY_ADMIN = "isAdmin";
    public static final String KEY_COMPANY = "isCompany";
    public static final String KEY_USER = "user";
    public static final String KEY_COLLECTION_CHAT = "chats";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "mensaje";
    public static final String KEY_TIMESTAMP = "fechaHora";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversaciones";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_LAST_MESSAGE = "ultimoMensaje";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String,String> getRemoteMsgHeaders(){

        if(remoteMsgHeaders == null){

            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,"key=AAAAJLUnjxI:APA91bF8XpwaMFdHO0J7bUz8VQFcSywN5WjUgH1trsQ-oGxRJS8jRUNmc-P_jmHEHSP0Nump7KyX4UehuLgekxM76W61GiWFvq33fS_Mhrytz5wf-0JG7KokK7sq8Jp21qNw6Mg645Ct");
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE,"application/json");

        }

        return remoteMsgHeaders;

    }

}

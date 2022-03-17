package com.ttco.uscdoordrink.database;

public interface UserProfileListener {
    // Returns null if query fails
    void onComplete(UserProfile userProfile);
}

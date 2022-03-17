package com.ttco.uscdoordrink.database;

public interface UsernameExistenceListener {
    // Returns null if query fails
    void onComplete(Boolean doesUsernameExist);
}

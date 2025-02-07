package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 05/02/2025 20:19
@Last Modified 05/02/2025 20:19
Version 1.0
*/
import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

public class BcryptCustom {
    private final int logRounds;

    public BcryptCustom(int logRounds) {
        this.logRounds = logRounds;
    }

    // Paul@123
    // 9812983719873
    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        if (BCrypt.checkpw(password, hash)) {
            int intRounds = getRounds(hash);
            if(intRounds ==0)
            {
                return false;
            }
            if (intRounds != logRounds) {
                String newHash = hash(password);
                return updateFunc.apply(newHash);
            }
            return true;
        }
        return false;
    }

    private int getRounds(String salt) {
        char minor = (char)0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
        {
            return 0;
        }
        if (salt.charAt(2) == '$')
        {
            off = 3;
        }
        else
        {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$')
            {
                return 0;
            }
            off = 4;
        }
        if (salt.charAt(off + 2) > '$')
        {
            return 0;
        }
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}

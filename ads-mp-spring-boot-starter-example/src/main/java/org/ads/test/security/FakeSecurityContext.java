/*
 * Copyright 2024 <player.sontan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ads.test.security;

import java.util.ArrayList;
import java.util.List;

public class FakeSecurityContext {

    private static final String USER_A = "Amy";
    private static final String USER_B = "Bob";
    private static final String USER_C = "Carl";

    private String currentUser;
    private List<Object> userClasses;
    private List<Object> userGrades;
    private List<Object> userTeachers;
    private boolean login;

    public FakeSecurityContext(String currentUser,
                               List<Object> userClasses,
                               List<Object> userGrades,
                               List<Object> userTeachers,
                               boolean login) {
        this.currentUser = currentUser;
        this.userClasses = userClasses;
        this.userGrades = userGrades;
        this.userTeachers = userTeachers;
        this.login = login;
    }

    public static FakeSecurityContext amy() {
        return new FakeSecurityContext(USER_A,
                new ArrayList<Object>(){{add(1);}},
                new ArrayList<Object>(){{add(1);}},
                new ArrayList<Object>(){{add(1);add(4);}},
                true);
    }

    public static FakeSecurityContext bob() {
        return new FakeSecurityContext(USER_B,
                new ArrayList<Object>(){{add(2);}},
                new ArrayList<Object>(){{add(1);}},
                new ArrayList<Object>(){{add(2);add(5);}},
                true);
    }

    public static FakeSecurityContext carl() {
        return new FakeSecurityContext(USER_C,
                new ArrayList<Object>(){{add(2);}},
                new ArrayList<Object>(){{add(2);}},
                new ArrayList<Object>(){{add(3);add(6);}},
                false);
    }

    public static FakeSecurityContext getCurrentUser() {
        return amy();
    }

    public boolean hasUserLogin() {
        return login;
    }

    public List<Object> getUserClasses() {
        return userClasses;
    }
}

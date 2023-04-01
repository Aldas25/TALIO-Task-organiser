/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardListOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddCardCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerLoginCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerSignUpCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminBoardOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(HelpScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardDeleteConfirmationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(CardListDeleteConfirmationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(BoardDeleteConfirmationCtrl.class).in(Scopes.SINGLETON);
    }
}
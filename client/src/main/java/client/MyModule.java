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
import client.services.*;
import client.utils.ImageUtils;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.util.ArrayList;
import java.util.List;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        List<Class> singletons = new ArrayList<>();
        singletons.addAll(getUtilClasses());
        singletons.addAll(getServiceClasses());
        singletons.addAll(getSingletonControllerClasses());
        singletons.forEach(aClass -> bindSingleton(binder, aClass));
    }

    private List<Class> getUtilClasses() {
        return List.of(
                ServerUtils.class,
                ImageUtils.class
        );
    }

    private List<Class> getServiceClasses() {
        return List.of(
                BoardService.class,
                CardService.class,
                JoinedBoardsService.class,
                ListService.class,
                DeleteService.class
        );
    }

    private List<Class> getSingletonControllerClasses() {
        return List.of(
                MainCtrl.class,
                CardListOverviewCtrl.class,
                ServerLoginCtrl.class,
                BoardOverviewCtrl.class,
                AdminBoardOverviewCtrl.class,
                HelpScreenCtrl.class,
                CardDeleteConfirmationCtrl.class,
                CardListDeleteConfirmationCtrl.class,
                BoardDeleteConfirmationCtrl.class
        );
    }

    private void bindSingleton(Binder binder, Class aClass) {
        binder.bind(aClass).in(Scopes.SINGLETON);
    }
}
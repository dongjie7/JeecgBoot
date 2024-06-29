/**
 *
 * (c) Copyright Ascensio System SIA 2021
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
 *
 */

package org.jeecg.modules.estar.nd.office.services.configurers.implementations;

import org.jeecg.modules.estar.nd.office.documentserver.models.configurations.Customization;
import org.jeecg.modules.estar.nd.office.documentserver.models.enums.Action;
import org.jeecg.modules.estar.nd.office.entities.User;
import org.jeecg.modules.estar.nd.office.services.configurers.CustomizationConfigurer;
import org.jeecg.modules.estar.nd.office.services.configurers.wrappers.DefaultCustomizationWrapper;
import org.jeecg.modules.flowable.apithird.entity.SysUser;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DefaultCustomizationConfigurer implements CustomizationConfigurer<DefaultCustomizationWrapper> {
    @Override
    public void configure(Customization customization, DefaultCustomizationWrapper wrapper) {  // define the customization configurer
        Action action = wrapper.getAction();  // get the action parameter from the customization wrapper
        SysUser user = wrapper.getUser();
        customization.setSubmitForm(action.equals(Action.fillForms) && "1".equals(user.getId()) && false);  // set the submitForm parameter to the customization config
    }
}

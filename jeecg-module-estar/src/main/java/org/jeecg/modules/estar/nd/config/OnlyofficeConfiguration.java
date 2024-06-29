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

package org.jeecg.modules.estar.nd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.estar.nd.office.documentserver.storage.FileStoragePathBuilder;
import org.jeecg.modules.estar.nd.office.documentserver.util.SSLUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class OnlyofficeConfiguration {

    @Value("${nbcio.files.storage}")
    private String storageAddress;

    @Value("${nbcio.files.docservice.verify-peer-off}")
    private String verifyPerrOff;

    @Autowired
    private FileStoragePathBuilder storagePathBuilder;

    @Autowired
    private SSLUtils ssl;

    @Bean
    public ModelMapper mapper(){  // create the model mapper
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()  // get the mapper configuration and set new parameters to it
                .setMatchingStrategy(MatchingStrategies.STRICT)  // specify the STRICT matching strategy
                .setFieldMatchingEnabled(true)  // define if the field matching is enabled or not
                .setSkipNullEnabled(true)  // define if null value will be skipped or not
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);  // specify the PRIVATE field access level
        return mapper;
    }

//    @Bean
//    public JSONParser jsonParser(){  // create JSON parser
//        return new JSONParser();
//    }

    @PostConstruct
    public void init(){  // initialize the storage path builder
        storagePathBuilder.configure(StringUtils.isEmpty(storageAddress) ? null : storageAddress);
        if(!verifyPerrOff.isEmpty()) {
            try{
                if(verifyPerrOff.equals("true")) {
                    ssl.turnOffSslChecking(); //the certificate will be ignored
                } else {
                    ssl.turnOnSslChecking(); //the certificate will be verified
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*@Bean
    public ObjectMapper objectMapper(){  // create the object mapper
        return new ObjectMapper();
    }*/
}

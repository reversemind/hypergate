package com.reversemind.glia.test.pojo.shared;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Copyright (c) 2013 Eugene Kalinin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public interface ISimplePojo extends Serializable {
    public List<PAddressNode> searchAddress(String query);

    public List<PAddressNode> searchAddress(String vv, List<String> query);

    public String createException(String query) throws SimpleException;
}

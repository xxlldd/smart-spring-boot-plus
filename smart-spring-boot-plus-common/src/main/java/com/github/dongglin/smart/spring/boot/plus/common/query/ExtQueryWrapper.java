/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.dongglin.smart.spring.boot.plus.common.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dongglin.smart.spring.boot.plus.common.parser.ParserCache;
import lombok.Getter;
import lombok.Setter;

/**
 * 动态查询wrapper
 *
 * @author Mazc@dibo.ltd
 * @version v2.0
 * @date 2020/04/16
 */
public class ExtQueryWrapper<T> extends QueryWrapper<T> {
    /**
     * 主实体class
     */
    @Getter
    @Setter
    private Class<T> mainEntityClass;

    /**
     * 获取entity表名
     *
     * @return
     */
    public String getEntityTable() {
        if (this.mainEntityClass == null) {
            this.mainEntityClass = getEntityClass();
        }
        return ParserCache.getEntityTableName(this.mainEntityClass);
    }
}

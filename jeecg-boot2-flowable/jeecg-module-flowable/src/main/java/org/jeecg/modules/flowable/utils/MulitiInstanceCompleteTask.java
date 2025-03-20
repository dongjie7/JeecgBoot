package org.jeecg.modules.flowable.utils;

import java.io.Serializable;

import org.flowable.engine.delegate.DelegateExecution;

/**
 * 多实例会签条件设置函数
 * @author nbacheng
 * @date 2022-09-22
 */
public class MulitiInstanceCompleteTask implements Serializable {
private static final long serialVersionUID = 1L;  
    
/**
 * 评估结果判定条件 同时<completionCondition>${这里可以设置成下面的函数进行特殊条件处理}</completionCondition>
 * @param execution 分配执行实例
 */

      public boolean accessCondition(DelegateExecution execution){ 
	      return false;  
      }
	  
      public boolean completeTask(DelegateExecution execution) {  
        return false;  
      } 
}

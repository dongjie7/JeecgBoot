import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '姓名',
    align: "center",
    dataIndex: 'name'
  },
  {
    title: '性别',
    align: "center",
    dataIndex: 'sex_dictText'
  },
  {
    title: '年龄',
    align: "center",
    dataIndex: 'age'
  },
  {
    title: '请假天数',
    align: "center",
    dataIndex: 'qianjiaNumber'
  },
  {
    title: '请假原因',
    align: "center",
    dataIndex: 'descripts'
  },
  {
    title: '开始时间',
    align: "center",
    dataIndex: 'startDate',
    customRender:({text}) =>{
      text = !text ? "" : (text.length > 10 ? text.substr(0,10) : text);
      return text;
    },
  },
  {
    title: '结束时间',
    align: "center",
    dataIndex: 'endDate',
    customRender:({text}) =>{
      text = !text ? "" : (text.length > 10 ? text.substr(0,10) : text);
      return text;
    },
  },
];

// 高级查询数据
export const superQuerySchema = {
  name: {title: '姓名',order: 0,view: 'text', type: 'string',},
  sex: {title: '性别',order: 1,view: 'list', type: 'string',dictCode: 'sex',},
  age: {title: '年龄',order: 2,view: 'number', type: 'number',},
  qianjiaNumber: {title: '请假天数',order: 3,view: 'number', type: 'number',},
  descripts: {title: '请假原因',order: 4,view: 'textarea', type: 'string',},
  startDate: {title: '开始时间',order: 5,view: 'date', type: 'string',},
  endDate: {title: '结束时间',order: 6,view: 'date', type: 'string',},
};

# test
daily test

#平安任职期间笔记
-- oracle WMSYS.WM_CONCAT 函数
该函数返回来自同一个分组的指定字段的非NULL值的连接起来字符串
参考：https://blog.csdn.net/guoxilen/article/details/77852762

-- oracle 替换函数replace和translate 函数
参考：https://www.cnblogs.com/xiaoqisfzh/p/5620890.html

-- oracle lpad 函数
select LPAD(GET_BATCHNO_SEQ.NEXTVAL, v_sequen_length, '0') from dual;

-- ibatis in语句参数传入方法
<select id="isNotNormal" resultClass="java.lang.Integer">
	select count(1)
  from claim_main cm, claim_workflow_task cwt
 where cm.docuno = cwt.docuno
   and cwt.workflow_task_type_code = '01'
   and cm.case_sts in ('04', '41')
   and cm.docuno in 
                <iterate open="(" close=")" conjunction="," > 
                    #[]# 
                </iterate> 
	</select>
https://blog.csdn.net/yangkai_hudong/article/details/25130555

-- ibatis 特殊字符处理
在ibatis的映射文件中出现了特殊字符的情况下会出现以上错误。
特殊字符有:  < 小于号 ,>大于号等
要用
<![CDATA[
select * from tb_users where id<=100
]]>
包裹着。

-- oracle 查看trigger触发器
select * from all_triggers where trigger_name = 'FAS_CONFIRMED_RECORD_AI'

select text from all_source where type='TRIGGER' AND name='FAS_CONFIRMED_RECORD_AI';

select * from ehis_tr_switch where trigger_name = 'FAS_CONFIRMED_RECORD_AI'（公司内部的表）
-- oracle null值得特殊处理
空值null比较特殊，它不能通过=或者<>进行查询，只能用is null或者is not null进行查询，例如你的数据中有null值，那么用 字段名=1，字段名<>1，字段名=null都不能把这条数据检索出来，只有字段名 is null能检索出来。
所以你需要查询的数据有两种，为null的，或者不等于1的，转化为sql就是
select * from 表 where 字段名 is null or 字段名 <> 1

-- oracle (+)是什么意思
oracle中的（+）是一种特殊的用法，（+）表示外连接，并且总是放在非主表的一方。
例如左外连接：
select A.a,B.a from A LEFT JOIN B ON A.b=B.b;
等价于
select A.a,B.a from A,B where A.b = B.b(+);

-- not in与not exists的区别
select PRODUCT_CODE from POL_MAIN where not exists(select plan_code from new_plan where POL_MAIN.PRODUCT_CODE = new_plan.plan_code) group by PRODUCT_CODE;

select PRODUCT_CODE from POL_MAIN where PRODUCT_CODE not in(select plan_code from new_plan) group by PRODUCT_CODE;
以上2句sql的效果基本一样，但是not exists多一个null值记录，not in则会将null记录排除。


-- 删除表并释放空间、删除公共同义词
DROP TABLE EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL
DROP PUBLIC SYNONYM CLAIM_LATERBATCH_ECASE_DETAIL

-- oracle在存储过程中动态拼调用其他存储过程语句
  v_procedure_sql := 'begin hcm_bill_G_calc.' ||
                       v_vip_proc_name ||
                       '(:v_polno, :v_certno, :v_listno, :v_brno, :v_plan_code, :v_docuno, :v_Injure_Rate, :v_err_flag, :v_err_msg); end;';
             execute immediate v_procedure_sql
              using IN p_polno, IN p_certno, IN p_listno, IN p_brno, IN p_plan_code, IN p_docuno, IN v_Injure_Rate, OUT p_err_flag, OUT p_err_msg;

-- 创建表
CREATE TABLE EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL
(
  ID_LATERBATCH_ECASE_DETAIL    varchar2(32),
  
  BATCHNO varchar2(20),
  
  DOCUNO        varchar2(16),
  
  CREATED_BY      varchar2(100),

  CREATED_DATE    date,

  UPDATED_BY      varchar2(100),

  UPDATED_DATE    date

)  initrans 6 ; 

CREATE UNIQUE INDEX PK_LATERBATCH_ECASE_DETAIL_ID ON CLAIM_LATERBATCH_ECASE_DETAIL(ID_LATERBATCH_ECASE_DETAIL);

ALTER TABLE CLAIM_LATERBATCH_ECASE_DETAIL ADD CONSTRAINT PK_LATERBATCH_ECASE_DETAIL_ID PRIMARY KEY (ID_LATERBATCH_ECASE_DETAIL) USING INDEX PK_LATERBATCH_ECASE_DETAIL_ID;

COMMENT ON TABLE  EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL IS 'E键赔事后对公支付批次明细表';

COMMENT ON COLUMN EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL.ID_LATERBATCH_ECASE_DETAIL IS '无意义主键';

COMMENT ON COLUMN EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL.BATCHNO IS '对公支付批次号';

COMMENT ON COLUMN EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL.DOCUNO IS '报案号';

COMMENT ON COLUMN EHISDATA.CLAIM_POL_BENEF_TMP.CREATED_BY IS '创建用户';

COMMENT ON COLUMN EHISDATA.CLAIM_POL_BENEF_TMP.CREATED_DATE IS '创建时间';

COMMENT ON COLUMN EHISDATA.CLAIM_POL_BENEF_TMP.UPDATED_BY IS '修改用户';

COMMENT ON COLUMN EHISDATA.CLAIM_POL_BENEF_TMP.UPDATED_DATE IS '修改时间';

create public synonym CLAIM_LATERBATCH_ECASE_DETAIL for EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL;

GRANT SELECT,INSERT,UPDATE,DELETE,REFERENCES ON EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL TO "EHISCDE";
grant references on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to ehisdata;
grant select, insert, update, delete on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_ehisdata_dml;
grant select on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_ehisdata_qry;
grant select on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_ehisdata_dev_qry;
grant select on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_himsdata_dev_qry;
grant select on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_hrcsdata_dev_qry;
grant select on ehisdata.CLAIM_LATERBATCH_ECASE_DETAIL to r_voudata_dev_qry;

create or replace trigger EHISTRG.LATERBATCH_ECASE_DETAIL_BI
before insert on EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL for each row
declare
  v_user_empno EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL.created_by%type;
begin
  v_user_empno := pub_sys_package.get_user;
  :new.created_by := v_user_empno;
  :new.created_date := sysdate;
  :new.updated_by := v_user_empno;
  :new.updated_date := sysdate;
end;
/

create or replace trigger EHISTRG.LATERBATCH_ECASE_DETAIL_BU
before update on EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL for each row
declare
  v_user_empno EHISDATA.CLAIM_LATERBATCH_ECASE_DETAIL.updated_by%type;
begin
  v_user_empno := pub_sys_package.get_user;
  :new.updated_by := v_user_empno;
  :new.updated_date := sysdate;
end;
/

initrans
数据库中，每个块都有一个块首部。这个块首部中有一个区域指向事务表中的某个条目。事务表中会建立一些条目来描述哪些事务将块上的哪些行/元素锁定。这个事务表的初始大小由对象的INITRANS 设置指定。
块首部作用：如果某个事务锁定了这个块的数据，则会在块的首部记录事务的标识，当然那个事务要先看一下这个地方是不是已经有人占用了，如果有，则去看看那个事务是否为活动状态。如果不活动，比如已经提交或者回滚，则可以覆盖这个地方。如果活动，则需要等待(闩的作用)。
简单说，该字段是为了解决资源争用的问题的。事务表初始大小越大，可以记录更多的事务信息，增加并发性能。
所以，如果有大量的并发访问使用的这个块，则参数不能太小，否则资源竞争将导致系统并发性能下降。
正常情况下，1就可以。一般默认为2。


Web层是基于spring mvc框架的，
参看以下文件：
web.xml、web-context-××.xml、tiles-defs-××.xml
具体到本EHIS-CLAIM项目
使用的分发器DispatcherServlet是EsgDispatcherServlet（继承了PAFA框架的PafaDispatcherServlet）作为接收前端的HTTP请求，根据BeanNameUrlHandlerMapping（默认的url匹配controller的mapping）来定位到controller处理请求，然后根据注册的HandlerAdapter匹配controller的类型来处理这个controller，HandlerAdapter会调用自己的handle方法，handle方法运用java的反射机制执行controller的具体方法来获得ModelAndView。
然后DispatcherServlet 依次调用doDispatch()→render()→resolveViewName()……这些方法，获得渲染需要的View对象，然后将model结合view对象渲染输出到response返回。
进入到controller层后，使用ACDispatcher查找绑定的Action调用具体的业务处理Action以及后续的一系列Service、Dao来处理业务（本质上是通过ApplicationController来连接的，具体后面分析）。

其中HandlerMapping，
      BeanNameUrlHandlerMapping 直接将url路径匹配和这个url一样的controller bean实例；
      DefaultAnnotationHandlerMapping 处理在类级别上的@RequestMapping注解，将url匹配到对应的controller bean实例；

其中HandlerAdapter，
      AnnotationMethodHandlerAdapter DispatcherServlet将带@controller注解的controller交给此HandlerAdapter处理，调用handle()方法定位到这个controller的具体某个方法获得ModelAndView对象；
      SimpleControllerHandlerAdapter 匹配实现javax.servlet.http.HttpServletRequest.Controller接口的controller
      HttpRequestHandlerAdapter 匹配处理实现java.io.IOException.HttpRequestHandler接口的controller

其中controller，
ParameterizableViewController 一般作为直接返回jsp页面的controller；
      SimpleFormController（只能接收POST请求参数，如果要接收GET传过来的参数，需要 重写isFormSubmission()方法） 一般作为简单的表单提交返回model+view的controller；
      ……
      如果自己要实现特殊的controller，可以直接继承AbstractController(POST/GET都可接收请求参数)；

防止表单重复提交的错误（避免多次处理同一个请求）
主要是在继承比如AbstractFormController（已被废弃）的controller中修改属性sessionForm、synchronizeOnSession为true。分别表示将表单数据保存在seesion中和如果同一个session有两个并行的提交，提交必须依次处理（相当于synchronized）。再重写handleInvalidSubmit()方法，对非法提交进行处理，可以返回一个提示错误的页面，或者返回输入页面。如果不覆盖handleInvalidSubmit方法，重复提交的请求仍然会当做正常的请求来处理。

sessionForm设为true，这样，在显示一个新表单时，Spring会将 command存放在session中，而在提交表单时，Spring会从session中取出此command，随后立即从session中删除存放 command的attribute。如果发现在session中没有command，Spring将其断定为重复提交，转而执行 handleInvalidSubmit(request, response)，可覆盖此方法负责防止重复提交的任务。

可以这么说，当setSessionForm(true)之后，如果没有先后经历显示表单、提交表单的过程，就会被认为是重复提交表单。


springmvc-Model和ModelAndView的区别
1、	Model只是用来传输数据的，并不会进行业务的寻址；但是，ModelAndView却是可以进行业务寻址的，就是设置对应的要请求的静态文件，这里的静态文件指的是类似jsp的文件；
2、	Model是每一次请求都必须会带着的，但是ModelAndView是需要我们自己去新建；
3、	model和session都是存放信息的地方，不同的地方就是他们的生命周期不同，model是request级别的；
4、	针对controller中方法返回视图名还是modelAndView，ModelAndViewResolver的resolveModelAndView()方法会做不同的处理，反正最后返回给DispatcherServlet的是modelAndView；



为啥修改jsp、js文件不需要重启weblogic（tomcat也一样）就能生效？
因为Tomcat对JSP进入了侦听，如果有修改，就会重新翻译成Servlet并最终编译成Class文件，替换掉原JSP页面对应的Class文件。Tomcat的内部机制是可以让这种Class文件立即生效的。而普通的Class文件修改后，不能立即生效。

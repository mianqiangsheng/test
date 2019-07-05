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

关于IDEA 导入Maven项目后不显示src目录结构解决办法
点击file->project structure..->Modules
点击右上角+加号 -》import Modules  选择项目。 一路next
 





Eclipse如何针对不同后缀名的文件使用不同的默认编辑器？比如xml文件使用xml编辑器。
① 首先安装xml editor插件，参看：https://blog.csdn.net/u014608640/article/details/52511153
② 如果下载插件的url无法连接，可以使用国内的镜像url（试了一下，没成功）
③ 最后设置*.xml后缀名的文件使用XML Editor(Default)打开编辑。参看：
https://www.cnblogs.com/likwo/archive/2012/05/04/2482298.html

查找指定内容所在的文件：ctrl+H
查找指定名字的文件：ctrl+shift+R
查找指定名字的类（包括jar包）：ctrl+shift+T

修改jsp显示的编码：
Preferences-General-Content Types—Default encoding


快速输出main函数、system.out.println等
输入main之后按"alt+/"组合键
输入sysout之后按"alt+/"组合键

 

Eclipse自定义设置快捷键
Window-->Preferences-->Java-->Code Style --> Code Templates --> Comments --> types --> Edit 
参看：https://www.cnblogs.com/ynavigatorqcarsauto/p/6184082.html

Eclipse中各种文件的注释与取消注释的快捷键

Java文件：
注释和取消注释的快捷键都是：CTRL + / 或 Shift+Ctrl+C

JS文件：
注释和取消注释的快捷键都是：CTRL + / 或 Shift+Ctrl+C

xml文件：
注释：选中要注销的代码CTRL + SHIFT + / 或 选中代码按Shift+Ctrl+C
取消注释：CTRL + SHIFT + \ 或 Shift+Ctrl+C

jsp文件：

html部分：
注释：选中要注销的代码CTRL + SHIFT + / 或 （不需选中代码）按Shift+Ctrl+C
取消注释：CTRL + SHIFT + \或Ctrl+Z（若是按Shift+Ctrl+C）

js部分、java部分：
注释：选中要注释的代码，按Shift+Ctrl+/ （被/* */注释）或按Shift+Ctrl+C（被//注释）
取消注释：若是按Shift+Ctrl+/被/* */注释的只能按Ctrl+Z撤销，若是//,则按Shift+Ctrl+C取消注释

html文件：

注释：Shift+Ctrl+C（不需选中代码） 或 选中要注销的代码CTRL + SHIFT + /
取消注释：CTRL + SHIFT + \ 或 Shift+Ctrl+C
--------------------- 
作者：ispotu 
来源：CSDN 
原文：https://blog.csdn.net/superit401/article/details/79172471 
版权声明：本文为博主原创文章，转载请附上博文链接！

eclipse bookmark的使用
1 插入bookmark

   把光标放在重要代码位置的最左边一栏，右击 add Bookmark，设置bookmark名称。

2 显示bookmark视图。

   点击eclipse菜单的window-》show view-》Bookmarks 将会显示bookmark视图。

3 定位bookmark

   双击bookmark里面的 名称，就能定位到那里去了。
--------------------- 
作者：wang2717 
来源：CSDN 
原文：https://blog.csdn.net/wang2717/article/details/6173883 
版权声明：本文为博主原创文章，转载请附上博文链接！




Linux学习：

什么是shell? bash和shell有什么关系？
参考：https://www.cnblogs.com/hihtml5/p/9272751.html

什么是Shell?
 
     shell是你（用户）和Linux（或者更准确的说，是你和Linux内核）之间的接口程序。你在提示符下输入的每个命令都由shell先解释然后传给Linux内核。
     shell 是一个命令语言解释器（command-language interpreter）。拥有自己内建的 shell 命令集。此外，shell也能被系统中其他有效的Linux 实用程序和应用程序（utilities and application programs）所调用。
    不论何时你键入一个命令，它都被Linux shell所解释。

shell 如何启动?
 
shell在你成功地登录进入系统后启动，并始终作为你与系统内核的交互手段直至你退出系统。你系统上的每位用户都有一个缺省的shell。每个用户的缺省shell在系统里的passwd文件里被指定，该文件的路径是/etc/passwd。passwd文件里还包含有其他东西：每个人的用户ID号，一个口令加密后的拷贝和用户登录后立即执行的程序，（注：为了加强安全性，现在的系统一般都把加密的口令放在另一个文件--shadow中，而passwd中存放口令的部分以一个x字符代替）虽然没有严格规定这个程序必须是某个Linux shell，但大多数情况下都如此。

最常用的shell:
  
在Linux 和 UNIX系统里可以使用多种不同的shell可以使用。最常用的几种是 Bourne shell (sh), C shell (csh), 和 Korn shell (ksh)。三种shell 都有它们的优点和缺点。Bourne shell 的作者是 Steven Bourne。它是 UNIX 最初使用的shell 并且在每种 UNIX 上都可以使用。Bourne shell 在 shell 编程方面相当优秀，但在处理与用户的交互方面作得不如其他几种 shell。 
    C shell 由 Bill Joy 所写，它更多的考虑了用户界面的友好性。它支持象命令补齐（command-line completion）等一些 Bourne shell 所不支持的特性。普遍认为C shell 的编程接口做的不如 Bourne shell, 但 C shell 被很多 C  程序员使用因为 C shell的语法和 C语言的很相似，这也是C shell名称的由来。
    Korn shell (ksh) 由 Dave Korn 所写。它集合了C shell 和 Bourne shell 的优点并且和 Bourne shell 完全兼容。
    除了这些 shell 以外，许多其他的 shell 程序吸收了这些原来的 shell 程序的优点而成为新的 shell 。在 Linux 上常见的有 tcsh (csh 的扩展)，Bourne Again shell(bash, sh 的扩展), 和Public Domain Korn shell (pdksh, ksh 的扩展)。bash 是大多数Linux 系统的缺省 shell。

bash的有用特性:

命令补齐（Command-Line Completion）
通配符
命令历史记录：
bash 把你先前输入的命令文本保存在一个历史列表中。当你用你的帐号登录后历史列表将根据一个历史文件被初始化。历史文件的文件名被一个叫 HISTFILE 的 bash变量指定。历史文件的缺省名字是 .bash_history。这个文件通常在你的用户目录($HOME)中。（注意该文件的文件名以一个句号开头，这意味着它是隐含的，仅当你带 -a 或 -A参数的 ls 命令列目录时才可见）
    仅将先前的命令存在历史文件里是没有用的，所以 bash 提供了几种方法来调用它们。使用历史记录列表最简单的方法是用上方向键。按下上方向键后最后键入的命令将出现在命令行上。再按一下则倒数第二条命令会出现，以此类推。如果上翻多了的话也可以用向下的方向键来下翻。（和 DOS 实用程序doskey一样）如果需要的话，显示在命令行上的历史命令可以被编辑。
命令别名：
假如为这个长命令建立一个名为goconfig的别名，在bash提示符下键入如下命令：
alias goconfig='cd /usr/X11/lib/X11/fvwm/sample-configs'
现在，除非你退出bash，键入goconfig将和原来的长命令有同样的作用。
如果想取消别名，可以使用下面的命令：
unalias goconfig
一些很多用户认为有用的别名，你可以把它们写入你的.profile文件中提高工作效。
输入重定向：
用于改变一个命令的输入源。
如果你仅在命令行上键入 wc <enter> ，wc 将等待你告诉它要统计什么，这时 bash 就好象死了一样，你键入的每样东西都出现在屏幕上，但什么事也不会发生。这是因为 wc 命令正在为自己收集输入。如果你按下Ctrl-D，wc 命令的结果将被写在屏幕上。如果你输入一个文件名做参数，象下面的例子一样，wc 将返回文件所包含的字符数，单词数，和行数： 
wc test
11 2 1
另一种把test文件内容传给 wc 命令的方法是重定向 wc 的输入。< 符号在bash里用于把当前命令的输入重定向为指定的文件。所以可以用下面的命令来把 wc 命令的输入重定向为 test 文件：
wc < test
11 2 1
输入重定向并不经常使用因为大多数命令都以参数的形式在命令行上指定输入文件的文件名。尽管如此，当你使用一个不接受文件名为输入参数的命令，而需要的输入又是在一个已存在的文件里时，你就能用输入重定向解决问题。
输出重定向：
输出重定向使你能把一个命令的输出重定向到一个文件里，而不是显示在屏幕上。
重定向举例，当你要把 ls 命令的输出保存为一个名为 directory.out 的文件时，你可以使用下面的命令：
ls > directory.out
管道：
管道可以把一系列命令连接起来。这意味着第一个命令的输出会通过管道传给第二个命令而作为第二个命令的输入，第二个命令的输出又会作为第三个命令的输入，以此类推。而管道行中最后一个命令的输出才会显示在屏幕上（如果命令行里使用了输出重定向的话，将会放进一个文件里）。 
你能通过使用管道符 | 来建立一个管道行，下面的示例就是一个管道行：
cat sample.text | grep "High" | wc -l
这个管道将把 cat 命令（列出一个文件的内容）的输出送给grep命令。grep 命令在输入里查找单词 High，grep命令的输出则是所有包含单词 High的行，这个输出又被送给 wc命令。带 -l选项的 wc命令将统计输入里的行数。假设 sample.txt的内容如下：
Things to do today:
Low: Go grocery shopping
High: Return movie
High: Clear level 3 in Alien vs. Predator
Medium: Pick up clothes from dry cleaner 
 
管道行将返回结果 2，指出你今天有两件很重要的事要做：
cat sample.text | grep "High" | wc -l
2
提示符：
bash 有两级用户提示符。第一级是你经常看到的 bash 在等待命令输入时的提示符。缺省的一级提示符是字符$（如果是超级用户，则是#号）。你可以通过改变bash 的PS1变量的值来改变你的缺省提示符，例如：
PS1="Please enter a command"
把bash shell 的提示符该为指定的字符串。 
  
当bash 期待输入更多的信息以完成命令时显示第二级提示符。缺省的第二级提示符是 >。 果你要改变第二级提示符，可以通过设置PS2变量的值来实现：
PS2="I need more information"
另外你还可以用特殊的字符来定义你的提示符，下面的列表列出了最常用的特殊字符。
提示符特殊字符代码
 字符      含义 
 /!       显示该命令的历史记录编号。 
 /#       显示当前命令的命令编号。 
  
 /$       显示$符作为提示符，如果用户是root的话，则显示#号。
 //       显示反斜杠。 
  
 /d       显示当前日期。 
  
 /h       显示主机名。 
  
 /n       打印新行。
 /nnn     显示nnn的八进制值。 
  
 /s       显示当前运行的shell的名字。
 /t       显示当前时间。
 /u       显示当前用户的用户名。
 /W       显示当前工作目录的名字。
 /w       显示当前工作目录的路径。 
 
这些特殊字符能组合成很多种有用的提示符方案（也可以组合为很奇异的方案），例如把 PS1 设为：
PS1="/t"
这导致提示符显示当前的时间，就象下面的显示一样（提示符后面将不会有空格）：
02:16:15 
 
而下面的设置：
PS1=/t
将导致提示符变成下面的样子：
t
这显示了设置中引号的重要性，下面的提示符串：
PS1="/t// "
会使提示符看起来象这个样子：
02:16:30/
这种情况下，提示符后面会有一个空格，因为引号里有一个空格。
作业控制（Job Control）：
作业控制能够控制当前正在运行的进程的行为。特别地，你能把一个正在运行的进程挂起，稍后再恢复它的运行。bash 保持对所有已启动的进程的跟踪，你能在一个正在运行的进程的生命期内的任何时候把它挂起或是使它恢复运行。
按下 Ctrl-Z 使一个运行的进程挂起。bg 命令使一个被挂起的进程在后台恢复运行（也可以在命令后面加上符号"&"，把命令放入后台执行），反之 fg 命令使进程在前台恢复运行。这几个命令在当用户想在后台运行而意外的把它放到了前台时，经常被用到。当一个命令在前台被运行时，它会禁止用户与 shell 的交互，直到该命令结束。这通常不会造成麻烦，因为大多数命令很快就执行完了。如果你要运行的命令要花费很长的时间的话，我们通常会把它放到后台，以使我们能在前台继续输入其他命令。例如，你输入这个命令：
command find / -name "test" > find.out
它将寻找整个文件系统中的名为test 的文件并把结果保存在一个叫fing.out的文件里。如果在前台运行的话，根据文件系统的大小，你的shell将有数秒甚至数分钟不能使用，你不想这样的话可以再输入以下面的内容：
control-z
bg
find 命令首先被挂起，再在后台继续被执行，并且你能马上回到bash下。
用户化配置bash：
已经描述的用户化配置bash的方法（例如上面我们刚刚提到的命令PS1="/t// " ）。但知道现在为止，我们所做的改动都仅在当前运行的bash下才有效。一旦退出系统，所有的改动也随之消失了。为了保存这些用户化配置，你必须把它们保存到一个bash的初始化文件里。 
    你能把任何想每次进入bash都执行的命令放到初始化文件里。这个文件里最常见到的命令通常是alias和变量的初始化。bash的初始化文件叫做 profile。每个使用bash的用户都有一个.profile文件在他的用户目录里（也可能是.bash_profile）。bash在每次启动时都读取这个文件，并执行所有包含的命令。
    下面的代码是缺省的.profile文件的内容。这个文件的位置在 /etc目录。如果你想设置自己的bash 的话把它拷到你的用户目录里（如果还没有的话）并命名为.profile。
     注意： 有些setup程序会在建立用户时自动放一个.profile文件的拷贝在你的用户目录里。但是并不是所有的都这么做，所以最好先检查一下你的用户目录。记住所有以句点开头的文件都是隐含的，只有用ls -a或ls -A命令才能列出。

      
linux里source、sh、bash、./有什么区别

在linux里，source、sh、bash、./都可以执行shell script文件，那它们有什么不同吗？

1、source

source a.sh
在当前shell内去读取、执行a.sh，而a.sh不需要有"执行权限"
source命令可以简写为"."
. a.sh
source 命令会强制执行脚本中的全部命令，而忽略脚本文件的权限。该命令主要用于让重新配置的环境变量配置文件强制生效，避免了注销或重启系统使环境变量配置文件生效。

2、sh/bash

sh a.sh
bash a.sh
都是打开一个subshell去读取、执行a.sh，而a.sh不需要有"执行权限"
通常在subshell里运行的脚本里设置变量，不会影响到父shell的。

3、./

./a.sh
#bash: ./a.sh: 权限不够
chmod +x a.sh
./a.sh
打开一个subshell去读取、执行a.sh，但a.sh需要有"执行权限"
可以用chmod +x添加执行权限。

 

4、fork、source、exec

使用fork方式运行script时， 就是让shell(parent process)产生一个child process去执行该script，当child process结束后，会返回parent process，但parent process的环境是不会因child process的改变而改变的。
使用source方式运行script时， 就是让script在当前process内执行， 而不是产生一个child process来执行。由于所有执行结果均于当前process内完成，若script的环境有所改变， 当然也会改变当前process环境了。
使用exec方式运行script时， 它和source一样，也是让script在当前process内执行，但是process内的原代码剩下部分将被终止。同样，process内的环境随script改变而改变。
通常如果我们执行时，都是默认为fork的。

为了实践下，我们可以先建立2个sh文件，以下代码来自ChinaUnix的網中人：

1.sh

复制代码
#!/bin/bash
A=B
echo "PID for 1.sh before exec/source/fork:$$"
export A
echo "1.sh: \$A is $A"
case $1 in
    exec)
        echo "using exec..."
        exec ./2.sh ;;
    source)
        echo "using source..."
        . ./2.sh ;;
    *)
        echo "using fork by default..."
        ./2.sh ;;
esac
echo "PID for 1.sh after exec/source/fork:$$"
echo "1.sh: \$A is $A"
复制代码
2.sh

#!/bin/bash
echo "PID for 2.sh: $$"
echo "2.sh get \$A=$A from 1.sh"
A=C
export A
echo "2.sh: \$A is $A"
 

自己运行下，观看结果吧 :)

chmod +x 1.sh
chmod +x 2.sh
./1.sh fork
./1.sh source
./1.sh exec


Shell（bash版本）基础知识
参考：http://c.biancheng.net/view/954.html

Shell的基本结构

第一行"#!/bin/bash"

在 Linux 中，以"#"开头的一般都是注释，不过这句话是例外的。这句话的作用是标称我以下写的脚本使用的是 Bash 语法，只要写的是基于 Bash 的 Shell 脚本都应该这样开头。这就像在 HTML 语言中嵌入 PHP 程序时，PHP 程序必须用<??>包含起来。
不过，有一些比较喜欢钻研的人也会有疑问，他们在写 Shell 脚本时，不加"#!/bin/bash"这句话，Shell 脚本也可以正确执行。那是因为我们是在默认 Shell 就是 Bash 的 Linux 中编写的脚本，而且脚本是纯 Bash 脚本才能够正确执行。如果把脚本放在默认环境不是 Bash 的环境中运行，又或者编写脚本的不是纯 Bash 语言，而是嵌入了其他语言（如 Tcl 语言），那么这个脚本就不能 正确执行了。所以，大家记住我们的 Shell 脚本都必须以"#!/bin/bash"开头。
第二行：注释

在 Shell 脚本中，除"#!/bin/bash"这行外，其他行只要以"#"开头的都是注释。第二行就是我们这个脚本的注释，建议大家在写程序时加入清晰而详尽的注释，这些都是建立良好编程规范时应该注意的问题。
第三行：程序的主体

既然 echo 命令可以直接打印"c.biancheng.net"，那么将这句话放入 Shell 脚本中也是可以正确执行的，因为 Linux 的命令是可以直接在脚本中执行的。


Shell（Bash）多命令顺序执行

多命令执行符	格 式	作 用
；	命令1 ; 命令2	多条命令顺序执行，命令之间没有任何逻辑关系
&&	命令1 && 命令2	如果命令1正确执行（$?=0)，则命令2才会执行
如果命令1执行不正确（$?≠0)，则命令2不会执行
||	命令1 || 命令2	如果命令1执行不正确（$?≠0)，则命令2才会执行
如果命令1正确执行（$?=0)，则命令2不会执行


父 Shell 和子 Shell【小括号和大括号用法及区别】
关于父 Shell 和子 Shell，大家可以想象成在 Windows 中我们开启了一个"cmd"字符操作终端，那么 Windows 本身就是父 Shell，而"cmd"终端则是子 Shell；也可以理解为在一个操作界面中又开启了一个操作界面。
知道了父 Shell 和子 Shell，我们接着解释小括号和大括号的区别。如果用于一串命令的执行，那么小括号和大括号主要区别在于：
() 执行一串命令时，需要重新开启一个子 Shell 来执行。
{} 执行一串命令时，在当前 Shell 中执行。
() 和 {} 都是把一串命令放田括号里面，并且命令之间用";"隔开。
() 最后一条命令可以不用分号。
{} 最后一条命令要用分号。
{} 的第一条命令和左括号之间必须有一个空格。
() 里的各命令不必和括号有空格。
() 和 {} 中括号里面的某条命令的重定向只影响该命令，但括号外的重定向则会影响到括号里的所有命令。


Shell（Bash）单引号、双引号和反引号用法
单引号 ’ ’ 括起来的字符都是普通字符，就算特殊字符也不再有特殊含义；
双引号 ” ” 括起来的字符中，"$"、"\"和反引号是拥有特殊含义的；
反引号 ` ` 代表引用命令；

Shell（Bash）变量及定义规范
1)	变量名可以由字母、数字和下画线组成，但是不能以数字开头。如果变量名是"2name"，则是错误的。
2)	在 Bash 中，变量的默认类型都是字符串型，如果要进行数值运算，则必须指定变量类型为数值型。
3)	变量用等号"="连接值，"="左右两侧不能有空格。
4)	变量值中如果有空格，则需要使用单引号或双引号包含。
5)	在变量值中，可以使用转义符"\"。
6)	如果需要増加变量值，那么可以进行变量叠加。变量叠加可以使用两种格式："$变量名"或 ${变量名}。
7)	如果要把命令的执行结果作为变量值赋予变量，则需要使用反引号或 $() 包含命令。
8)	环境变量名建议大写，便于区分（与之相对的是用户自定义变量，用户自定义变量只在当前的 Shell 中生效，而环境变量会在当前 Shell 和这个 Shell 的所有子 Shell 中生效）。
9)	需要提取变量中的内容时，需要在变量名之前加入"$"符号。
10)	set命令可以用来查看系统中的所有变量（用户自定义变量和环境变量），env 命令进行环境变量的查询。
11)	unset命令删除自定义变量，环境变量也一样。
12)	使用export声明的变量就是环境变量。


PATH变量用法
一般情况下shell脚本运行需要指定绝对路径或者相对路径，但是通过把这些脚本存放的路径放入到PATH变量中去，就可以像系统自带的shell脚本命令一样直接使用了。

[root@localhost ~]# echo $PATH
/usr/lib/qt-3.3/bin:/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/
bin:/root/bin
PATH 变量的值是用":"分隔的路径，这些路径就是系统查找命令的路径。

可以把shell脚本文件放到已有的路径中去，也可以添加自定义的路径通过变量叠加到PATH路径中去。
[root@localhost ~]# PATH="$PATH":/root/sh
#在变量PATH的后面，加入/root/sh目录


Shell（Bash）位置参数变量
在 Linux 的命令行中，当一条命令或脚本执行时，后面可以跟多个参数，我们使用位置参数变量来表示这些参数。
其中，$0 代表命令行本身，$1 代表第 1 个参数，$2 代表第 2 个参数，依次类推。当参数个数超过 10 个时，就要用大括号把这个数字括起来，例如，${10} 代表第 10 个参数。

位置参数变量	作 用
$n	n 为数字，$0 代表命令本身，$1〜$9 代表第 1〜9 个参数，10 以上的参数需要用大括号包含， 如${10}
$*	这个变量代表命令行中所有的参数，把所有的参数看成一个整体
$@	这个变量也代表命令行中所有的参数，不过 $@ 把每个参数区别对待
$#	这个变量代表命令行中所有参数的个数


Shell预定义变量用法
预定义变量是在 Shell 一开始时就定义的变量，这一点和默认环境变量有些类似。不同的是，预定义变量不能重新定义，用户只能根据 Shell 的定义来使用这些变量。
预定义变量	作 用
$?	最后一次执行的命令的返回状态。如果这个变量的值为 0，则证明上一条命令正确执行；如果这 个变量的值为非 0 (具体是哪个数由命令自己来决定)，则证明上一条命令执行错误
$$	当前进程的进程号（PID)
$!	后台运行的最后一个进程的进程号（PID)


Shell read命令详解：接收键盘或其它文件描述符的输入
前面讲过，位置参数变量是可以把用户的输入用参数的方式输入脚本的，不过这种输入方式只有写这个脚本的人才能确定需要输入几个参数，每个参数应该输入什么类型的数据，并不适合普通用户使用。
除位置参数变量外，我们也可以使用 read 命令向脚本中传入数据。read 命令接收标准输入（键盘）的输入，或者其他文件描述符的输入。得到输入后，read 命令将数据放入一个标准变量中。


Shell数值运算方法（3种）
1、	使用declare声明变量类型
root@localhost ~]# aa=11
[root@localhost ~]# bb=22
#给变量aa和bb赋值
[root@localhost ~]# declare -i cc=$aa+$bb #声明变量cc的类型是整数型，它的值是aa和bb的和

特别的，除了声明为整数型变量，declare可以声明数组类型变量、只读变量、环境变量。其中数组变量如下声明：
[root@localhost ~]# name[0]="zhang san"
#数组中第一个变量是张三
[root@localhost ~]# name[1]="li ming"
#数组中第二个变量是李明
[root@localhost ~]# name[2]="gao luo feng"
#数组中第三个变量是高洛峰
[root@localhost ~]# echo ${name}
zhang san
#输出数组的内容。如果只写数组名，那么只会输出第一个下标变量
[root@localhost ~]# echo ${name[*]}
zhang san li ming gao luo feng
#输出数组所有的内容
注意，数组的下标是从 0 开始的，在调用数组的元素时，需要使用"${数组[下标]}"方式来读取。
或者
declare -a name='([0]="zhang san" [1]="li ming" [2]="gao luo feng")'

2、	使用 exp 命令
[root@localhost ~]# aa=11
[root@localhost ~]# bb=22
#给变量aa和bb赋值
[root@localhost ~]# dd=$(expr $aa + $bb)
#dd的值是aa和bb的和。注意"+"号左右两侧必须有空格
3、	使用"$((运算式))"或"$[运算式]"
[root@localhost ~]# aa=11
[root@localhost ~]# bb=22
[root@localhost ~]# ff=$(( $aa+$bb))
[root@localhost ~]# echo $ff
33
#变量ff的值是aa和bb的和
[root@localhost ~]# gg=$[ $aa+$bb ]
[root@localhost ~]# echo $gg
33
#变量gg的值是aa和bb的和


Shell常用运算符
使用时也要按"$((运算式))"或"$[运算式]"这样的格式使用
优先级	运算符	说 明
13	-,+	单目负、单目正
12	!,~	逻辑非、按位取反或补码
11	*, /, %	乘、除、取模
10	+, -	加、减
9	<<, >>	按位左移、按位右移
8	<=, >=, <, >	小于或等于、大于或等于、小于、大于
7	== ,!=	等于、不等于
6	&	按位与
S	^	按位异或
4	|	按位或
3	&&	逻辑与
2	||	逻辑或
1	=,+=,•=，*=,/=,%=,&=, |=, <<=, >>=	赋值、运算且赋值


Shell变量测试与内容置换
在脚本中，有时需要判断变量是否存在或是否被赋予了值，如果变量已经存在并且被赋予了值，则不改变变量；如果变量不存在或没有被赋值，则赋予其新值。这时我们就可以使用变量测试与内容置换。

我们在脚本中可以使用条件判断语句 if 来替代这种测试方法，不过使用 Shell 自带的变量置换更加方便。
变量置换方式	变量y没有设置	变量y为空值	变量y设置值
x=${y-新值}	x= 新值	x 为空	x=$y
x=${y:-新值}	x= 新值	x= 新值	x=$y
x=${y+新值}	x 为空	x= 新值	x=新值
x=${y:+新值}	x 为空	x 为空	x=新值
x=${y=新值}	x= 新值	x 为空	x=$y
	y= 新值	y 值不变	y值不变
x=${y:=新值}	x= 新值	X= 新值	x=$y
	y= 新值	y= 新值	y值不变
x=${y?新值}	新值输出到标准错误输出（屏幕）	x 为空	x=$y
x=${y:?新值}	新值输出到标准错误输出	新值输出到标准错误输出	x=$y


Shell环境变量配置文件及其分类
对于linux启动后的一些环境变量（比如 PATH、HISTSIZE、PS1、HOSTNAME 等）的初始化（常态化），需要将这些设置固定下来，而linux启动时会按一定顺序调用这些配置文件。

在 Linux 系统登录时主要生效的环境变量配置文件有以下 5 个：
/etc/profile：对所有的登录用户生效；
/etc/profile.d/*.sh（一系列以.sh结尾的配置文件）：对所有的登录用户生效；
~/.bash_profile：只会对当前用户生效（因为每个用户的家目录中都有这两个文件）
-/.bashrc：只会对当前用户生效（因为每个用户的家目录中都有这两个文件）
/etc/bashrc：对所有的登录用户生效；

环境变量配置文件读取流程：
 缺失图1

1、用户登录过程中先调用 /etc/profile 文件。在这个环境变量配置文件中会定义如下默认环境变量。
-USER 变量：根据登录的用户给这个变量赋值（就是让 USER 变量的值是当前用户）。
-LOGNAME 变量：根据 USER 变量的值给这个变量赋值。
-MAIL 变量：根据登录的用户来定义用户的邮箱为 /var/spool/mail/ 用户名。
-PATH 变量：根据登录用户的 UID 是否为 0，判断 PATH 变量是否包含 /sbin、/usr/sbin 和 /usr/local/sbin 这三个系统命令目录。
-HOSTNAME 变量：根据主机名给这个变量赋值。
-HISTSIZE 变量：定义历史命令的保存条数。
-umask：定义 umask 默认权限。注意：/etc/profile 文件中的 umask 权限是在"有用户登录过程（输入了用户名和密码）"时才会生效的。

2、/etc/profile调用 /etc/profile.d/*.sh文件，也就是调用 /etc/porfile.d/ 目录下所有以 .sh 结尾的文件。最常用的就是 lang.sh 文件，而这个文件又会调用 /etc/sysconfig/i18n 文件。/etc/sysconfig/i18n 就是前面讲过的默认语系配置文件。
/etc/profile同时调用~/.bash_profile文件，这个文件主要实现了两个功能：
1、调用了 ~/.bashrc 文件。
2、在 PATH 变量后面加入了":$HOME/bin"这个目录。也就是说，如果我们在自己的家目录中建立了 bin 目录，然后把自己的脚本放入"~/bin"目录中，就可以直接执行脚本，而不用通过目录执行了。

3、~/.bash_profile 文件调用 -/.bashrc 文件。在 -/.bashrc 文件中主要实现了两个功能：
1、定义默认别名。笔者把自己定义的别名也放入了这个文件。
2、调用/etc/bashrc。

4、~/.bashrc 文件调用 /etc/bashrc 文件。在 /etc/bashrc 文件中主要实现了如下功能：
1、PS1 变量：也就是用户的提示符。如果我们想要永久修示符，就要在这个文件中修改。
2、umask：定义 umask 默认权限。这个文件中定义的 umask 是针对"没有用户登录过程（不需要输入用户名和密码，比如从一个终端切换到另一个终端，或进入子Shell）"时生效的。如果是"有用户登录过程"，则 /etc/profile 文件中的 umask 生效。
3、PATH 变量：会给 PATH 变量追加值，当然也是在"没有用户登陆过程"时才调用的。
4、调用 /etc/profile.d/*.sh 文件，这也是在"没有用户登录过程"时才调用的。在"有用户登录过程"时，/etc/profile_d/*.sh 文件已经被 /etc/profile 文件调用过了。

以上是linux启动时加载的配置文件，登录用户注销退出时只会调用一个环境变量配置文件，就是 ~/.bash_logout。这个文件默认没有写入任何内容，但是如果我们希望在退出登录时执行一些操作，比如清除历史命令、备份某些数据，就可以把命令写入这个文件。

还有一些其他的环境变量配置文件，诸如~/bash_history 文件，也就是历史命令保存文件。


Linux注意事项
1、Linux 不靠扩展名区分文件类型，即使诸如*.gz、*.bz2、*.zip、*.tar.gz、*.tar.bz2、*.tgz、*.sh也只是为了帮助管理员来区分不同的文件类型，不是必须的；
2、Linux 中所有内容（包括硬件设备）以文件形式保存；
3、inux中所有存储设备都必须在挂载之后才能使用，所有文件都放置在以根目录为树根的树形目录结构中。在 Linux 看来，任何硬件设备也都是文件，它们各有自己的一套文件系统（文件目录结构）。因此产生的问题是，当在 Linux 系统中使用这些硬件设备时，只有将Linux本身的文件目录与硬件设备的文件目录合二为一，硬件设备才能为我们所用。合二为一的过程称为“挂载”； 


Linux文件目录结构一览表
Linux 根目录（/）
 缺失图2 表1 Linux一级目录及其作用 表2 其他一级目录及功能
 

Linux /usr目录
 缺失图3 表3 /user子目录及其功能

Linux /var 目录
 缺失图4 表4 /var子目录及其功能

      
Linux软件包
Linux下的软件包可细分为两种，分别是源码包和二进制包。这些软件包几乎都是经 GPL 授权、免费开源（无偿公开源代码）的。这意味着如果你具备修改软件源代码的能力，只要你愿意，可以随意修改。
源码包：安装软件需要编译器进行编译（编译器是指该源码包特定的编译器？），费时易出错，但是给你完全个性化配置甚至修改源码的权限；
二进制包：发布时已经完成了预编译，安装方便快速，但看不到源码无法修改（流行使用RPM 包管理系统进行管理）；

Linux RPM包统一命名规则
RPM 二进制包命名的一般格式如下：
包名-版本号-发布次数-发行商-Linux平台-适合的硬件平台-包扩展名（.rpm 扩展名——二进制包；src.rpm 扩展名的——源码包）

二进制RPM包所有安装文件会按类别分散安装到默认安装路径，可通过命令查询此RPM包的默认路径：
缺失图5 表1 RPM包默认安装路径
 
源码包采用手动指定安装路径（习惯安装到 /usr/local/ 中），linux中可以同时存在以二进制包和源码包安装的程序，但同时只能启动一个。

二进制包和源码包的安装在rpm命令下有一定的差异。具体查看参考网页。（这里所说的源码包本质上还是rpm包，与后面通过make命令安装的源码包不是一样的，这里的源码包仅仅是源代码被压缩后的文件）

进一步，RPM不能解决包依赖的问题，所以有了yum，全称“Yellow dog Updater, Modified”，是一个专门为了解决包的依赖关系而存在的软件包管理器。就好像 Windows 系统上可以通过 360 软件管家实现软件的一键安装、升级和卸载，Linux 系统也提供有这样的工具，就是 yum。
yum的使用需要配置yum源，即软件包下载的来源。可以配置网络yum源、本地yum源。

因为很多linux上的的程序是使用C语言写的，所以在使用源码包安装时，一般都要先安装C语言的编译器——gcc（如果是其他语言写的源码包就要下载其他相应的编译器咯？）。
所以一般在正式安装其他软件时，首先用yum命令安装gcc（rpm安装yum，yum安装gcc，因为gcc涉及的依赖较多）。
除此之外，要实现对涉及C语言的源码包的安装，光有编译器gcc还不够，还需要安装 make 编译命令。这是因为一般源码包中还有很多复杂关联的源码文件，需要决定编译先后顺序，make命令正式代替手动自动完成这一任务（rpm安装yum）。

对于一个典型的C语言写的程序源码包的安装过程如下：
确保使用rpm命令安装了gcc编译器；
确保使用rpm命令安装了make命令；
解压源码包压缩文件；
执行里面的configure命令，指定程序源码包安装路径，一般在/usr/local/**目录下；
执行make命令进行编译；
执行make install命令，这里通常会写清程序的安装位置（和configure命令时的路径是什么关系？），如果没有，则建议把安装的执行过程保存下来，以备将来删除软件时使用；
安装源码包过程中，如果出现“error”（或“warning”）且安装过程停止，表示安装失败；反之，如果仅出现警告信息，但安装过程还在继续，这并不是安装失败，顶多使软件部分功能无法使用；
注意，如果在 "./configure" 或 "make" 编译中报错，则在重新执行命令前一定要执行 make clean 命令，它会清空 Makefile 文件或编译产生的 ".o" 头文件；

卸载源码包安装的程序，只需要找到软件的安装位置(比如/usr/local/apache2)，直接删除所在目录即可，不会遗留任何垃圾文件。需要注意的是，在删除软件之前，应先将软件停止服务。

Linux 系统中更新用源码包安装的软件，除了卸载重装这种简单粗暴的方法外，还可以下载补丁文件更新源码包，用新的源码包重新编译安装软件。比较两种方式，后者更新软件的速度更快——因为打补丁更新软件的过程比安装软件少了 "./configure" 步骤，且编译时也只是编译变化的位置，编译速度更快。
相关命令：diff 命令、patch命令

综上：linux安装程序有2中方式：
使用RPM包安装（容易安装、卸载和升级的特点，而且还提供查询和验证的功能，安装时更有数字证书的保护；需自己解决软件依赖性【如果使用yum命令使用rpm包安装也会提供自动的依赖管理，这里所说的是当不提供这种依赖管理的情况】）
下载源码压缩文件使用特定编译器运行make命令自行编译安装（肯定能安装成功；耗时）
总的来说，优先使用rpm包安装，当这一选项不可选时才考虑下载源码包安装。

      除了以上使用二进制包和源码包进行程序的安装，也可以像Windows 下的程序安装，有一个可执行的安装程序，只要运行安装程序，然后进行简单的功能定制选择（比如指定安装目录等)，就可以安装成功，只不过是在字符界面完成的。
      比如安装一个叫作 Webmin 的工具软件，只需要解压安装文件后运行setup.sh这个shell脚本即可按照软件开商发布安装脚本进行自动的安装。


Linux函数库（静态函数库和动态函数库）
函数库就是一些函数的集合，每个函数都具有独立的功能且能被外界调用。我们在编写代码时，有些功能根本不需要自己实现，直接调用函数库中的函数即可。
需要注意的是，函数库中的函数并不是以源代码的形式存在的，而是经过编译后生成的二进制文件，这些文件无法独立运行，只有链接到我们编写的程序中才可以运行。
Linux 系统中的函数库分为 2 种，分别是静态函数库（简称静态库，文件通常命名为 libxxx.a（xxx 为文件名））和动态函数库（也称为共享函数库，简称动态库或共享库，文件通常命名为 libxxx.so.major.minor（xxx 为文件名，major 为主版本号，minor 为副版本号）），两者的主要区别在于，程序调用函数时，将函数整合到程序中的时机不同：
静态函数库在程序编译时就会整合到程序中，换句话说，程序运行前函数库就已经被加载。这样做的好处是程序运行时不再需要调用外部函数库，可直接执行；缺点也很明显，所有内容都整合到程序中，编译文件会比较大，且一旦静态函数库改变，程序就需要重新编译。
动态函数库在程序运行时才被加载（如图所示），程序中只保存对函数库的指向（程序编译仅对其做简单的引用）。
缺失图5
 
Centos中可以直接使用yum命令安装特定的函数库；使用ldd命令可以查看某个程序调用到的函数，比如“ldd /bin/ls”可以查看ls命令的调用的函数；也可以手动将函数库拷贝到"/usr/lib" 或 "/lib" 中，"/etc/ld.so.conf" 文件，ldconfig


linux对于文件,目录,r,w,x权限分别表示什么意思?
r(Read，读取)：对文件而言，具有读取文件内容的权限；对目录来说，具有浏览目 录的权限。
w(Write,写入)：对文件而言，具有新增、修改文件内容的权限；对目录来说，具有删除、移动目录内文件的权限。
x(eXecute，执行)：对文件而言，具有执行文件的权限；对目录了来说该用户具有进入目录的权限。

Linux用户和用户组
/etc/passwd 文件，是系统用户配置文件，存储了系统中所有用户的基本信息。

[root@localhost ~]# vi /etc/passwd
#查看一下文件内容
root:x:0:0:root:/root:/bin/bash
bin:x:1:1:bin:/bin:/sbin/nologin
daemon:x:2:2:daemon:/sbin:/sbin/nologin
adm:x:3:4:adm:/var/adm:/sbin/nologin
...省略部分输出...

每一行记录了一个用户，用户信息都以 "：" 作为分隔符，划分为7个字段，每个字段所表示的含义如下：
用户名：密码：UID（用户ID）：GID（组ID）：描述性信息：主目录：默认Shell

用户名：仅仅为了方便用户记忆，linux是根据UID来识别用户身份的；
密码：”X”表示此用户设有密码，但真正密码保存在	/etc/shadow 文件中，只有root用户可以浏览操作。如果没有“X”，表示此用户没有密码，可以不输密码登录，但是远程登录还是不行的。
UID：0~65535 之间的数，不同范围的数字表示不同的用户身份，其中0表示管理员账号，1~499为系统用户（伪用户，供系统程序使用的用户），其他为普通用户。
GID：这里显示的是用户的初始组，每个用户只有一个初始组，可以加入多个附加组。
描述性信息：无意义，仅做解释用。
主目录：用户登录后有操作权限的访问目录，通常称为用户的主目录。root 超级管理员账户的主目录为 /root，普通用户的主目录为 /home/yourIDname。
默认Shell：Linux 系统默认使用的命令解释器是 bash（/bin/bash），当然还有其他命令解释器，例如 sh、csh 等

用户密码以SHA512 散列加密算法保存在/etc/shadow 文件中，同 /etc/passwd 文件一样，文件中每行代表一个用户，同样使用 ":" 作为分隔符，不同之处在于，每行用户信息被划分为 9 个字段。每个字段的含义如下：
用户名：加密密码：最后一次修改时间：最小修改时间间隔：密码有效期：密码需要变更前的警告天数：密码过期后的宽限时间：账号失效时间：保留字段
加密密码前如果有"!!"，表示此密码失效，用户不能登录。

如果普通用户忘记密码，只要通过root用户重新配置即可（使用passwd命令）；
如果 root 账号的密码遗失，则需要重新启动进入单用户模式，系统会提供 root 权限的 bash 接口，此时可以用 passwd 命令修改账户密码；也可以通过挂载根目录，修改 /etc/shadow，将账户的 root 密码清空的方法，此方式可使用 root 无法密码即可登陆，建议登陆后使用 passwd 命令配置 root 密码。

/etc/group是记录组 ID（GID）和组名相对应的文件。每一行各代表一个用户组。
各用户组中，还是以 "：" 作为字段之间的分隔符，分为 4 个字段，每个字段对应的含义为：
组名：密码：GID：该用户组中的用户列表

/etc/gshadow存储组用户的密码信息（组密码有什么用？在哪里使用？）。每行代表一个组用户的密码信息，各行信息用 ":" 作为分隔符分为 4 个字段，每个字段的含义如下：
组名：加密密码：组管理员：组附加用户列表

添加新的用户
useradd命令、/etc/default/useradd 文件
修改用户密码
passwd命令
修改用户信息
usermod命令
修改用户密码状态
chage命令，主要用来批量创建用户后要求这些用户第一次登录强制让其修改初始密码。Example：chage -d 0 lamp
删除用户
userdel命令，仅root用户可使用此命令，

userdel 命令的作用于以下文件中，删除与指定用户有关的数据信息：
用户基本信息：存储在 /etc/passwd 文件中；
用户密码信息：存储在 /etc/shadow 文件中；
用户群组基本信息：存储在 /etc/group 文件中；
用户群组信息信息：存储在 /etc/gshadow 文件中；
用户个人文件：主目录默认位于 /home/用户名，邮箱位于 /var/spool/mail/用户名。

实际中，如果要删除的用户已经使用过系统一段时间，那么此用户可能在系统中留有其他文件，因此，如果我们想要从系统中彻底的删除某个用户，最好在使用 userdel 命令之前，先通过 find -user 用户名 命令查出系统中属于该用户的文件，然后在加以删除。

用户间切换（包含su和su -的区别）
su命令
选项：
-：当前用户不仅切换为指定用户的身份，同时所用的工作环境也切换为此用户的环境（包括 PATH 变量、MAIL 变量等），使用 - 选项可省略用户名，默认会切换为 root 用户。
-l：同 - 的使用类似，也就是在切换用户身份的同时，完整切换工作环境，但后面需要添加欲切换的使用者账号。
-p：表示切换为指定用户的身份，但不改变当前的工作环境（不使用切换用户的配置文件）。
-m：和 -p 一样；
-c 命令：仅切换用户执行一次命令，执行后自动切换回来，该选项后通常会带有要执行的命令。

普通用户之间切换以及普通用户切换至 root 用户，都需要知晓对方的密码，只有正确输入密码，才能实现切换；从 root 用户切换至其他用户，无需知晓对方密码，直接可切换成功。

su – 等价于 su – root；
su - -c "useradd user1" root ： 切换为root用户执行"useradd user1"命令后即切回原来的用户；
su root ： 切换到root，但是没有切换环境变量，比如PATH 变量、MAIL 变量还是原来用户的变量；

sudo命令
使用su命令可以让普通用户切换到root身份，但是存在一些问题，首先是一经授权即将所有的root操作赋给了普通用户，有风险；其次是使用su命令切换意味着普通用户知晓root用户密码，有风险。
sudo 命令的运行只需要知道自己的密码即可，甚至于，我们可以通过手动修改 sudo 的配置文件，使其无需任何密码即可运行。

[root@localhost ~]# sudo [-b] [-u 新使用者账号] 要执行的命令
常用的选项与参数：
-b  ：将后续的命令放到背景中让系统自行运行，不对当前的 shell 环境产生影响。
-u  ：后面可以接欲切换的用户名，若无此项则代表切换身份为 root 。
-l：此选项的用法为 sudo -l，用于显示当前用户可以用 sudo 执行那些命令。

sudo命令的运行，默认需经历如下几步：
当用户运行 sudo 命令时，系统会先通过 /etc/sudoers 文件，验证该用户是否有运行 sudo 的权限；
确定用户具有使用 sudo 命令的权限后，还要让用户输入自己的密码进行确认。出于对系统安全性的考虑，如果用户在默认时间内（默认是 5 分钟）不使用 sudo 命令，此后使用时需要再次输入密码；
密码输入成功后，才会执行 sudo 命令后接的命令。

能否使用 sudo 命令，取决于对 /etc/sudoers 文件的配置（默认情况下，此文件中只配置有 root 用户）。通过修改该文件，可以设置哪些用户或者哪些组可以使用sudo命令，可以赋给其什么身份已经以切换身份后可以执行哪些命令。

修改 /etc/sudoers 文件的命令如下：
[root@localhost ~]# visudo
…省略部分输出…
root ALL=(ALL) ALL  <--大约 76 行的位置
# %wheel ALL=(ALL) ALL   <--大约84行的位置
#这两行是系统为我们提供的模板，我们参照它写自己的就可以了

[root@localhost ~]# ls -l
total 36
drwxr-xr-x. 2 root root 4096 Apr 15 16:33 Desktop
drwxr-xr-x. 2 root root 4096 Apr 15 16:33 Documents
...
-rwxr-xr-x. 2 root root 4096 Apr 15 16:33 post-install
drwxrwx---+ 2 root tgroup 4096 Apr 16 12:55 /project
...
权限前的字符，表示文件的具体类型，比如 d 表示目录，- 表示普通文件，l 表示连接文件，b 表示设备文件，等等；
如果在权限位后面多了一个"+"，表示此目录拥有ACL权限；
执行位除x外，我们有时会看到 s（针对可执行文件或目录，使文件在执行阶段，临时拥有文件所有者的权限）和 t（针对目录，任何用户都可以在此目录中创建文件，但只能删除自己的文件），文件设置 s 和 t 权限，会占用 x 权限的位置；
Linux SetUID（SUID）文件特殊权限——S（参照应用实例passwd命令，非root用户也可以执行只有root拥有权限的shell命令）
SUID（S权限）具有如下特点：
只有可执行文件才能设定 SetUID 权限，对目录设定 SUID，是无效的。
用户要对该文件拥有 x（执行）权限。
用户在执行该文件时，会以文件所有者的身份执行。
SetUID 权限只在文件执行过程中有效，一旦执行完毕，身份的切换也随之消失。
因为S权限的特殊性，所以使用不当会给linux系统带来很大的潜在风险。因此不建议轻易给可执行文件增加S权限，当系统首次安装好后马上查找系统中所有拥有 SetUID 和 SetGID 权限的文件，把它们记录下来，作为扫描的参考模板。如果某次扫描的结果和本次保存下来的模板不一致，就说明有文件被修改了 SetUID 和 SetGID 权限，需要排查修改的原因并修复文件权限。
***现提供备份和扫描脚本如下***
[root@localhost ~]# find / -perm -4000 -o -perm -2000 > /root/suid.list
#-perm安装权限査找。-4000对应的是SetUID权限，-2000对应的是SetGID权限
#-o是逻辑或"or"的意思。并把命令搜索的结果放在/root/suid.list文件中
接下来，只要定时扫描系统，然后和模板文件比对就可以了。脚本如下：
[root@localhost ~]#vi suidcheck.sh
#!/bin/bash
find / -perm -4000 -o -perm -2000 > /tmp/setuid.check
#搜索系统中所有拥有SetUID和SetGID权限的文件，并保存到临时目录中
for i in $(cat /tmp/setuid.check)
#循环，每次循环都取出临时文件中的文件名
do
    grep $i /root/suid.list > /dev/null
    #比对这个文件名是否在模板文件中
    if ["$?"!="o"]
    #检测测上一条命令的返回值，如果不为0，则证明上一条命令报错
    then
        echo "$i isn't in listfile! " >>/root/suid_log_$(date +%F)
        #如果文件名不在模板文件中，则输出错误信息，并把报错写入日志中
    fi
done
rm -rf/tmp/setuid.check
#删除临时文件
[root@localhost ~]# chmod u+s /bin/vi
#手工给vi加入SetUID权限
[root@localhost ~]# ./suidcheck.sh
#执行检测脚本
[root@localhost ~]# cat suid_log_2013-01-20
/bin/vi isn't in listfile!
#报错了，vi不在模板文件中。代表vi被修改了SetUID权限

Linux SetGID（SGID）文件特殊权限：组权限的’x’用’s’替代。类似SUID作用
[root@localhost ~]# ll /usr/bin/locate
-rwx--s--x. 1 root slocate 35612 8月24 2010 /usr/bin/locate

Linux Stick BIT（SBIT）文件特殊权限——t（参照存储临时文件的 /tmp 目录, 用户在此目录下只能操作自己创建的文件或目录，而无法修改甚至删除其他用户创建的文件或目录）——
正常情况下，用户以目录所属组或其他人的身份进入 A 目录时，如果甲对该目录有 w 权限，则表示对于 A 目录中任何用户创建的文件或子目录，甲都可以进行修改甚至删除等操作。

修改文件和目录的所属组
chgrp命令
修改文件和目录的所有者和所属组
chown命令，可以实现chgrp命令的功能，但是修改所属组还是推荐使用chgrp命令。

rwx权限对文件和目录的不同作用：对目录拥有w权限，不管有没有对该目录下文件的w权限，也可以进行删除操作；对目录拥有x权限，代表可以使用cd命令进入目录。
修改文件或目录的权限
chmod命令
r --> 4, w --> 2,  x --> 1：chmod 777 .bashrc
+：加入权限，-：删除权限，=：设定权限 chmod u-s /usr/bin/passwd
u、g、o 分别代表 3 种身份，还用 a 表示全部的身份（all 的缩写）：chmod u=rwx,go=rx .bashrc
新建文件和目录拥有默认权限
以上的内容是针对现有文件或者文件夹进行权限相关的修改操作，针对新建的文件或者文件夹，linux系统是使用umask默认权限来赋予初始权限的（Windows 系统中，新建的文件和目录时通过继承上级目录的权限获得的初始权限）。——新建文件和目录默认是创建者的userId和groupId，同时只要某个用户拥有对某个目录的W权限，参照文件夹的W权限定义，该用户就已经有删除修改该目录下内容的权限了，和这里讨论的默认权限无关。
[root@localhost ~]# umask
0022
#root用户默认是0022，普通用户默认是 0002
#后3位数字 "022" 才是真正要用到的 umask 权限值，将其转变为字母形式为 ----w--w-

文件和目录的真正初始权限，可通过以下的计算得到：
文件（或目录）的初始权限 = 文件（或目录）的最大默认权限 - umask权限
对文件来讲，其可拥有的最大默认权限是 666，即 rw-rw-rw-；
对目录来讲，其可拥有的最大默认权限是 777，即 rwxrwxrwx；

以 umask 值为 022 为例，分别计算新建文件和目录的初始权限：
文件的最大默认权限是 666，换算成字母就是 "-rw-rw-rw-"，umask 的值是 022，换算成字母为 "-----w--w-"。把两个字母权限相减，得到 (-rw-rw-rw-) - (-----w--w-) = (-rw-r--r--)

ACL权限
当linux提供的传统的3种身份（文件所有者，所属群组，其他用户）不满足需求时，可以使用ACL访问控制权限来实现，即针对某一个文件或者文件夹对特定用户给予特定的访问权限。

CentOS 6.x 系统中，ACL 权限默认处于开启状态，无需手工开启。
要查看ACL权限是否开启，使用mount命令查看到系统中已经挂载的分区，dumpe2fs 命令可以查看到这个分区文件系统的详细信息。
[root@localhost ~]# mount
/dev/sda1 on /boot type ext4 (rw)
/dev/sda3 on I type ext4 (rw)
…省略部分输出…
#使用mount命令可以看到系统中已经挂载的分区，但是并没有看到ACL权限的设置
[root@localhost ~]# dumpe2fs -h /dev/sda3
#dumpe2fs是查询指定分区文件系统详细信息的命令
…省略部分输出…
Default mount options: user_xattr acl
…省略部分输出…

如果 Linux 系统如果没有默认挂载，可以执行如下命令实现手动挂载：
[root@localhost ~]# mount -o remount,acl /
#重新挂载根分区，并加入ACL权限
使用 mount 命令重新挂载，并加入 ACL 权限。但使用此命令只是临时生效，要想永久生效，需要修改 /etc/fstab 文件，修改方法如下：
[root@localhost ~]#vi /etc/fstab
UUID=c2ca6f57-b15c-43ea-bca0-f239083d8bd2 /ext4 defaults,acl 1 1
#加入ACL权限，手工在 defaults 后面加入 "，acl" 即可永久在此分区中开启 ACL 权限。
[root@localhost ~]# mount -o remount /
#重新挂载文件系统或重启系统，使修改生效
ACL权限设置（setfacl和getfacl）
setfacl -m：给用户或群组添加 ACL 权限 —— setfacl -m u[g]:st[groupst]:rx /project
setfacl -d：设定默认 ACL 权限 —— 默认文件夹下新建子文件不继承ACL权限，使用此命令可设置默认ACL权限，注意这一命令只对文件夹生效。setfacl -m d:u[g]:st[groupst]:rx project
setfacl -R：设定递归 ACL 权限 —— 父目录在设定 ACL 权限时，所有的子文件和子目录也会拥有相同的 ACL 权限。setfacl -m u[g]:st[groupst]:rx -R project
setfacl -x：删除指定的 ACL 权限 —— setfacl -x u[g]:st[groupst] project
setfacl -b：删除指定文件的所有 ACL 权限 —— setfacl -b project
ACL权限的上限：mask有效权限
mask::rwx <-mask 权限
user:st:r-x <-用户 st 的权限
即指定给st的ACL权限不能超过mask规定的最大ACL权限。以上最终用户st获得的ACL权限就是r-x。
如果要修改mask权限，执行以下命令即可；
[root@localhost ~]# setfacl -m m:rx /project
#设定mask权限为r-x，使用"m:权限"格式

修改文件系统的权限属性
管理 Linux 系统中的文件和目录，除了可以设定普通权限和特殊权限外，还可以利用文件和目录具有的一些隐藏属性。比如指定文件或目录是否可修改、删除等。
chattr命令，只有 root 用户可以使用。如果要查看这些隐藏属性，可用lsattr命令。

Linux文件系统管理
硬盘进行格式化：
不仅清除了硬盘中的数据，还向硬盘中写入了文件系统。因为不同的操作系统，管理系统中文件的方式也不尽相同（给文件设定的属性和权限也不完全一样），因此，为了使硬盘有效存放当前系统中的文件数据，就需要将硬盘进行格式化，令其使用和操作系统一样（或接近）的文件系统格式。
现实中，对电脑分成C、D、E、F四个分区，然后重装系统时只会格式化C盘，清除数据并写入文件系统。但MBR没有改变？一个分区可以理解为一个文件系统（现在一个文件系统可以由几个分区组成，或者一个分区可以格式化为几个不同的文件系统），其他3个分区没有格式化，还是使用的和windows系统一致的文件系统，所以在重装系统后可以继续使用。

MBR，全称 Master Boot Record，可译为硬盘主引导记录，占据硬盘 0 磁道的第一个扇区。MBR 中，包括用来载入操作系统的可执行代码，实际上，此可执行代码就是 MBR 中前 446 个字节的 boot loader 程序（引导加载程序），而在 boot loader 程序之后的 64 个（16×4）字节的空间，就是存储的分区表（Partition table）相关信息。如图所示。
缺失图6 图1 MBR结构示意图
 
在分区表（Partition table）中，主要存储的值息包括分区号（Partition id）、分区的起始磁柱和分区的磁柱数量。所以 Linux 操作系统在初始化时就可以根据分区表中以上 3 种信息来识别硬盘设备。其中，常见的分区号如下：
0x5（或 0xf）：可扩展分区（Extended partition）。
0x82：Linux 交换区（Swap partition）。
0x83：普通 Linux 分区（Linux partition）。
0x8e：Linux 逻辑卷管理分区（Linux LVM partition）。
0xfd：Linux 的 RAID 分区（Linux RAID auto partition）。


为什么要将一个硬盘划分成多个分区，而不是直接使用整个硬盘呢？其主要有如下原因：
方便管理和控制
首先，可以将系统中的数据（也包括程序）按不同的应用分成几类，之后将这些不同类型的数据分别存放在不同的磁盘分区中。由于在每个分区上存放的都是类似的数据或程序，这样管理和维护就简单多了。
提高系统的效率
给硬盘分区，可以直接缩短系统读写磁盘时磁头移动的距离，也就是说，缩小了磁头搜寻的范围；反之，如果不使用分区，每次在硬盘上搜寻信息时可能要搜寻整个硬盘，所以速度会很慢。另外，硬盘分区也可以减轻碎片（文件不连续存放）所造成的系统效率下降的问题。
使用磁盘配额的功能限制用户使用的磁盘量
由于限制用户使用磁盘配额的功能，只能在分区一级上使用，所以，为了限制用户使用磁盘的总量，防止用户浪费磁盘空间（甚至将磁盘空间耗光），最好将磁盘先分区，然后在分配给一般用户。
便于备份和恢复
硬盘分区后，就可以只对所需的分区进行备份和恢复操作，这样的话，备份和恢复的数据量会大大地下降，而且也更简单和方便。

查看文件系统硬盘使用情况
df 命令，从各文件系统（分区）的 Super block（超级块）中读取数据，显示linux系统各文件系统（分区）的硬盘使用情况。
缺失图7 /home、/boot、/dev/shm这些目录虽然都在根目录/下，但是挂载在了不同的硬盘分区中，也就是说这些目录下的文件独立于根目录中其他文件的存放。 /proc、/sys等这些特殊的linux目录数据存放于内存中，所以硬盘显示容量为零。
 

统计目录或文件所占磁盘空间大小
df 命令从文件系统的角度来查看当前系统在硬盘上的使用情况，du命令则从某个具体的目录或者文件角度查看其占用的硬盘空间大小情况。
有时我们会发现，使用 du 命令和 df 命令去统计分区的使用情况时，得到的数据是不一样的。那是因为df命令是从文件系统的角度考虑的，通过文件系统中未分配的空间来确定文件系统中已经分配的空间大小。也就是说，在使用 df 命令统计分区时，不仅要考虑文件占用的空间，还要统计被命令或程序占用的空间（最常见的就是文件已经删除，但是程序并没有释放空间）。
而 du 命令是面向文件的，只会计算文件或目录占用的磁盘空间。也就是说，df 命令统计的分区更准确，是真正的空闲空间。

另外，需要注意的是，使用"ls -r"命令是可以看到文件的大小的。但是大家会发现，在使用"ls -r"命令査看目录大小时，目录的大小多数是 4KB，这是因为目录下的子目录名和子文件名是保存到父目录的 block（默认大小为 4KB）中的，如果父目录下的子目录和子文件并不多，一个 block 就能放下，那么这个父目录就只占用了一个 block 大小。
缺失图8 du命令 统计当前目录的总磁盘占用量大小
 

挂载Linux系统外的文件
首先了解下为什么有挂载这个概念——挂载指的是将硬件设备的文件系统和 Linux 系统中的文件系统，通过指定目录（作为挂载点）进行关联。

Linux系统中所有的硬件设备必须挂载之后才能使用，只不过，有些硬件设备（比如硬盘分区）在每次系统启动时会自动挂载（系统开机时会主动读取 /etc/fstab 这个文件中的内容，根据该文件的配置，系统会自动挂载指定设备），而有些（比如 U 盘、光盘）则需要手动进行挂载（注意，修改完 /etc/fatab 文件后，务必要亲自测试一下，有问题赶紧处理，因为此文件修改错误，将直接导致系统无法启动。）。

[root@localhost ~]# mount [-t 系统类型] [-L 卷标名] [-o 特殊选项] [-n] 设备文件名 挂载点
-o iocharset=utf8：指定挂载硬件设备时使用UTF-8编码格式
如果不知道设备文件名，可使用fdisk 命令进行查看（fdisk -l）。

为什么使用 Linux 系统的硬盘分区这么麻烦，而不能像 Windows 系统那样，硬盘安装上就可以使用？ 
其实，硬盘分区（设备）挂载和卸载（使用 umount 命令）的概念源自 UNIX，UNIX 系统一般是作为服务器使用的，系统安全非常重要，特别是在网络上，最简单有效的方法就是“不使用的硬盘分区（设备）不挂载”，因为没有挂载的硬盘分区是无法访问的，这样系统也就更安全了。
另外，这样也可以减少挂载的硬盘分区数量，相应地，也就可以减少系统维护文件的规模，当然也就减少了系统的开销，即提高了系统的效率。

注意，Linux 默认是不支持 NTFS 文件系统的，所以默认是不能挂载 NTFS 格式的移动硬盘的。要想让 Linux 支持移动硬盘，主要有三种方法：
重新编译内核，加入 ntfs 模块，然后安装 ntfs 模块即可；
不自己编译内核，而是下载已经编译好的内核，直接安装即可；
安装 NTFS 文件系统的第三方插件，也可以支持 NTFS 文件系统；

给硬盘分区
在 Linux 中有专门的分区命令 fdisk 和 parted。其中 fdisk 命令较为常用，但不支持大于 2TB 的分区；如果需要支持大于 2TB 的分区，则需要使用 parted 命令，当然 parted 命令也能分配较小的分区。我们先来看看如何使用 fdisk 命令进行分区。

注意，千万不要在当前的硬盘上尝试使用 fdisk，这会完整删除整个系统，一定要再找一块硬盘，或者使用虚拟机。
1、	创建主分区
2、	创建扩展分区（一块硬盘上？主分区+扩展分区最多只能建立4个，其中扩展分区只能建立一个）
3、	创建逻辑分区（扩展分区不能直接被格式化和使用，必须在扩展分区内部再建立逻辑分区）
4、	为分区写入文件系统，mkfs命令
5、	在使用fdisk命令过程中，只要不是输入“w”进行保存是不会生效的，有时需要重启系统才能使新的分区表生效（如果没有提示重启系统，则直接格式化即可）（如果不想重新启动，则可以使用 partprobe 命令。如果这个命令不存在，则请安装 parted-2.1-18.el6.i686 这个软件包）


parted命令支持给2TB的硬盘做分区。
1、	修改成gpt分区，mklabel gpt（如果这块硬盘上已经有分区了，那么原有分区和分区中的数据都会消失，而且需要重启系统才能生效（一定要把 /etc/fstab 文件和原有分区中的内容删除才能重启），转换分区表的目的是支持大于 2TB 的分区，如果分区并没有大于 2TB，那么这一步是可以不执行的）
2、	建立分区，mkpart
3、	建立文件系统，mkfs
4、	调整分区大小，resize（一定要先卸载分区，再调整分区大小，否则数据是会出现问题）
5、	删除分区， rm
6、	parted 中所有的操作都是立即生效的，没有保存生效的概念

ps：给一块硬盘分区时不是一定要把所有资源都分配到已创建的分区中去，也可以留下空闲的空间以备以后再调整分区时使用。

格式化分区（为分区写入文件系统）
mkfs 命令、mke2fs命令（手动调整分区默认参数）

Linux swap分区
我们在安装系统的时候已经建立了 swap 分区。swap 分区通常被称为交换分区，这是一块特殊的硬盘空间，即当实际内存不够用的时候，操作系统会从内存中取出一部分暂时不用的数据，放在交换分区中，从而为当前运行的程序腾出足够的内存空间。
1、	分区，使用fdisk 命令或parted 命令，建立主分区，设置大小，调整分区id为82；
2、	格式化swap分区，mkswap命令；
3、	添加新建的swap分区到已有的swap分区中，swapon命令；
4、	卸载添加的swap分区，swapoff命令；
5、	查看当前内存和swap分区使用情况，free 命令；
6、	如果想让 swap 分区开机之后自动挂载，就需要修改 /etc/fstab 文件，将新建的swap分区加入进去；

磁盘配额
磁盘配额（Quota）就是 Linux 系统中用来限制特定的普通用户或用户组在指定的分区上占用的磁盘空间或文件个数的（仅针对文件系统进行限制，意味着如果想对某个目录进行配额，需要此目录挂载的分区是独立的）。
磁盘配额实施步骤：
1、	当前linux系统内核必须支持磁盘配额（grep CONFIG_QUOTA /boot/corrfig-2.6.32-279.el6.i686）；
2、	安装Quota工具（rpm -qa | grep quota）；
3、	拟进行分区的目录是挂载在独立的硬盘分区下；
[root@localhost ~]# df -h /home
Filesystem     Size  Used Avail Use% Mounted on
/dev/hda3      4.8G  740M  3.8G  17% /home  <-- /home 确实是独立的！
4、	确认硬盘分区的文件系统支持磁盘配额，比如VFAT 文件系统不支持；
5、	要支持磁盘配额的分区必须开启磁盘配额功能；
手动开启（重新挂载此分区会失效）：
[root@localhost ~]# mount -o remount,usrquota,grpquota /home
[root@localhost ~]# mount | grep home
/dev/hda3 on /home type ext3 (rw,usrquota,grpquota)
自动开启（重新挂载分区仍有效）：
[root@www ~]# vi /etc/fstab
......
LABEL=/home   /home  ext3   defaults,usrquota,grpquota  1 2
[root@www ~]# umount /home
[root@www ~]# mount -a
[root@www ~]# mount | grep home
/dev/hda3 on /home type ext3 (rw,usrquota,grpquota)

6、	扫描文件系统并建立Quota记录文件，quotacheck命令;
7、	开启磁盘配额限制，quotaon命令；
8、	关闭磁盘配额限制，quotaoff命令；
9、	修改用户（群组）的磁盘配额（交互式），edquota命令；
10、	设置磁盘配额（非交互式），setquota命令；
11、	查询磁盘配额，quota命令——查询用户或用户组配额，repquota命令 ——查询文件系统配额


LVM逻辑卷管理机制（硬盘分区管理机制）
以上介绍的传统硬盘分区在需要调整重新规划分区大小时会不够灵活（随着业务的增加，文件系统负载会越来越大，当到了空间不足的情况时，如果我们还在使用传统的分区方式管理硬盘，就不得不将现有的所有分区全部删除，并重新规划新的存储方案；parted 虽然可以调整分区大小并不会删除已有数据，但是它需要卸载分区之后才可以进行，也就是说需要停止服务）。
LVM 是 Logical Volume Manager 的简称，译为中文就是逻辑卷管理。它是 Linux 下对硬盘分区的一种管理机制。LVM 适合于管理大存储设备，并允许用户动态调整文件系统的大小。此外，LVM 的快照功能可以帮助我们快速备份数据。LVM 为我们提供了逻辑概念上的磁盘，使得文件系统不再关心底层物理磁盘的概念。

LVM 是在硬盘分区之上建立一个逻辑层，这个逻辑层让多个硬盘或分区看起来像一块逻辑硬盘，然后将这块逻辑硬盘分成逻辑卷之后使用，从而大大提高了分区的灵活性。我们把真实的物理硬盘或分区称作物理卷（PV）；由多个物理卷组成一块大的逻辑硬盘，叫作卷组（VG）；将卷组划分成多个可以使用的分区，叫作逻辑卷（LV）。而在 LVM 中最小的存储单位不再是 block，而是物理扩展块（Physical Extend，PE）。我们通过图 1 看看这些概念之间的联系。

 缺失图 9 
图 1 LVM 示意图
 
物理卷（Physical Volume，PV）：就是真正的物理硬盘或分区。
卷组（Volume Group，VG）：将多个物理卷合起来就组成了卷组。组成同一个卷组的物理卷可以是同一块硬盘的不同分区，也可以是不同硬盘上的不同分区。我们可以把卷组想象为一块逻辑硬盘。
逻辑卷（Logical Volume，LV）：卷组是一块逻辑硬盘，硬盘必须分区之后才能使用，我们把这个分区称作逻辑卷。逻辑卷可以被格式化和写入数据。我们可以把逻辑卷想象为分区。
物理扩展（Physical Extend，PE）：PE 是用来保存数据的最小单元，我们的数据实际上都是写入 PE 当中的。PE 的大小是可以配置的，默认是4MB。

也就是说，我们在建立 LVM 的时候，需要按照以下步骤来进行：
1、	把物理硬盘分成分区，当然也可以是整块物理硬盘；
使用fdisk 交互命令创建物理分区，分区的系统 ID 不再是 Linux 默认的分区 ID 83，而要改成 LVM 的 ID 8e
2、	把物理分区建立为物理卷（PV），也可以直接把整块硬盘都建立为物理卷；
使用pvcreate命令将物理分区设成物理卷（pvscan命令和pvdisplay命令都可以查看物理卷状态，pvremove命令删除物理卷，前提是必须先将其所在的卷组删除）
3、	把物理卷整合为卷组（VG）。卷组就已经可以动态地调整大小了，可以把物理分区加入卷组，也可以把物理分区从卷组中删除；
建立卷组，vgcreate命令；
激活/停用卷组，vgchange命令；
查看卷组，vgscan 命令和vgdisplay 命令；
增加卷组容量，vgextend命令；
减少卷组容量，vgreduce命令；
删除卷组，vgremove命令；
4、	把卷组再划分为逻辑卷（LV），当然逻辑卷也是可以直接调整大小的。我们说逻辑卷可以想象为分区，所以也需要格式化和挂载；
建立逻辑卷，lvcreate命令；
格式化逻辑卷并挂载，mkfs和mount命令；
查看逻辑卷，lvscan和lvdisplay命令；
调整逻辑卷大小，lvresize+ resize2fs命令；（不建议缩小逻辑卷大小，可能会造成数据丢失）
删除逻辑卷，lvremove命令；（首先要卸载挂载该逻辑卷的文件目录，umount命令）


RAID磁盘列阵
以上介绍的关于对硬盘使用的内容有一个最大的问题是硬盘一旦损坏数据即丢失。
为此，使用RAID（Redundant Arrays of Inexpensive Disks，磁盘阵列）的优势在于硬盘读写性能更好，而且有一定的数据冗余功能。【RAID 功能已经内置在 Linux 2.0及以后的内核中，为了使用这项功能，还需要特定的工具来管理 RAID，在绝对多数 Linux 发行版本中，更多的是使用 mdadm，读者可以自行下载并安装这个工具。】

RAID 将几块独立的硬盘（不同的分区也可以组成 RAID）组合在一起，形成一个逻辑上的 RAID 硬盘，这块“硬盘”在外界（用户、LVM 等）看来，和真实的硬盘一样，没有任何区别。

RAID 根据组合方式的不同，有多种设计解决方案，以下介绍几种常见的 RAID 方案（RAID级别）。
RAID 0：也叫 Stripe 或 Striping（带区卷），是 RAID 级别中存储性能最好的一个。由n块的硬盘（分区）组成，数据切段分别存储在不同硬盘（分区）上，写入速度理论上是单独一块硬盘（分区）的n倍，容量也是n倍，但是数据丢失风险同时也是n倍。
 

RAID 1：也叫 Mirror 或 Mirroring（镜像卷），由两块硬盘组成。数据同时保存2份，具有数据冗余功能，多进程读取同一数据性能有提高，但是实际使用容量只有两块硬盘加起来的一半，且因为同一份数据写了2遍，所以写入性能较差。
 

RAID 10 或 RAID 01：集合RAID 0和RAID 1的优点，在性能和安全性上做一定的折中平衡。
 

RAID 5：最少需要由 3 块硬盘组成，当然硬盘的容量也应当一致。数据分段每次循环写入数据的过程中，在其中一块硬盘中加入一个奇偶校验值（Parity）【这个奇偶校验值就是其他两块硬盘中的数据经过换算之后产生的】，这个奇偶校验值的内容是这次循环写入时其他硬盘数据的备份。当有一块硬盘损坏时，采用这个奇偶校验值进行数据恢复。
具备数据冗余功能，不论该RAID一共有多少块硬盘（分区），只需要一块硬盘（分区）即可实现；读写性能近似于RAID 0；总可用容量为n-1块硬盘（分区）空间大小；缺点是只有在损坏一块硬盘（分区）的情况下才能恢复数据。

至于想要在服务器上使用RAID磁盘阵列，可选软RAID——通过软件实现RAID功能，性能较差 和硬 RAID——通过专门的RAID卡来实现RAID功能，性能好得多但是RAID卡昂贵。

TIPS：
访问国际知名的 Netcraft 网站 http:// www.netcraft.com，在 "What's that site running?" 的地址栏内输入想了解信息的网站地址（该功能位于网页右侧），单击箭头图标即可搜索到相关信息（网站拥有方、运行服务器操作系统等）。
缺失图10 如何选择开源协议

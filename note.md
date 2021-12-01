HttpSecurity可以添加以下的配置项
*openidLogin 有一种名为OpenID身份认证机制，没用过。在spring security里这一对配置项已经被标为废弃了
*headers "Adds the Security headers to the response"，为http响应添加关于安全的headers？
*cors 添加一个CorsFilter
*sessionManagement ...
*portMapper http和https端口的映射，不知道干嘛用
*jee 在请求到达spring security之前已经由servlet容器完成了认证之类的
*x509 基于公钥的认证...
*rememberMe ...
*authorizeRequests
*requestCache 请求页面时如果没有认证，就将请求重定向至登录界面，提交了登录信息并且通过了认证，再重定向至本来想去的页面，这个选项就是用来控制存放那个“本来想去的界面”的
*exceptionHandling 配置请求被拦截之后的处理
*securityContext 关于设置SecurityContext，不懂
*servletApi 似乎只能设置一个rolePrefix
*csrf ...
*logout ...
*anonymous 可以设置匿名用户的权限之类
*formLogin 设置登录界面、登陆成功/失败重定向地址、http请求数据中用户名和密码分别对应的字段名
*saml2Login 关于SAML 2.0之类的，不懂

oauth2所谓的四种类型，说的都是从Authorization Server获取token的方式，获取到token后的操作都一样
我们开发的系统扮演的是Client的角色，

oauth2授权码模式的步骤：
1.User访问Client，Client给出一个页面，页面上列举了几种第三方的认证服务，例如google，github等
2.User点击了其中一个认证服务的图标，比如github，这个图标是个<a>标签，它的href属性为
"https://oauth.github.com/oauth/authorize?response_type=code&client_id=CLIENT_ID&redirect_uri=CALLBACK_URL1&scope=read",
所以点击之后浏览器就向这个地址发去了请求，然后收到了一个页面，这个页面中有用户名和密码的输入框
3.User输入自己的用户名和密码，点击登录，然后浏览器页面出现了一些提示信息，询问User是否同意授予XXX获取XXX数据的权限
4.用户点确定按钮，浏览器向oauth.github.com发送提交“确定”这一信息。oauth.github.com收到确定信息之后，返回一个redirect响应，这个响应的Location头是这样的：
Location：CALLBACK_URL1?code=1238912
CALLBACK_URL1是Client自己指定的，在第2步发给了oauth.github.com
5.浏览器收到oauth.github.com的redirect响应，向"CALLBACK_URL1?code=1238912"这一地址也就是Client发起请求，Client就拿到了code，此时可以给前端返回页面，也可以先不返回，让用户稍等一下
6.Client在后台拿着code向oauth.github.com发送一个GET请求，url为
"https://oauth.github.com/oauth/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=authorization_code&code=1238912&redirect_uri=CALLBACK_URL2"
7.oauth.github.com收到请求，直接让这个http请求先响应回去了，即仅返回一个200的状态
8.oauth.github.com生成一个Token，发送给地址CALLBACK_URL2，此地址是在第6步Client告诉oauth.github.com的，至此Client就获取到了Access Token

*oauth2Login 想用这个功能需要加入spring-boot-starter-oauth2-client 
    (1)tokenEndpoint:这个端点指的是步骤6中Authorization Server负责接受”颁发token申请“的端点。 此配置项智能配置向Authorization Server获取Token的RestTemplate，对于oauth2的四种模式，spring security相应地给出了一些RestTemplate
    (2)redirectionEndpoint:这个配置的意义有点特别，配置的是接收来自Authorization Server的token数据的，本地的端点，也就是步骤8中的CALLBACK_URL2
    (3)authorizationEndpoint:这个端点指的Authorization Server上提供code的端点 配置与Authorization Server的认证端点有关的事。！！！！！！！！这个端点是不是在自己充当Authorization Server角色才用的？
    (4)userInfoEndpoint:配置Authorization Server提供个人用户信息的端点，这个端点不是rfc6749中定义的，似乎是spring security在OICD方面的扩展
    (5)loginPage:似乎又是作为Authorization Server才需要的，提供一个包含用户名密码输入框的页面
    (6)loginProcessingUrl:同上，loginPage提交的用户名密码需要提交到此处？应该是对应于步骤4
    
(1)、(3)、(4)中的“endpoint”指的都是Authorization Server的，(2)的endpoint是自己的

*oauth2Client 想用这个功能需要加入spring-boot-starter-oauth2-client，配置的是自己作为一个Client时需要用到的东西
    (1)authorizedClientRepository
    (2)authorizedClientService:会覆盖authorizedClientRepository的配置，这一项和上一项都是配置对于“authorized client”的管理
    (3)clientRegistrationRepository：里边维护着本系统在其它各个Authorization Server上注册之后获得的clientId,clientSecret，比如在谷歌注册的、在github注册的等等
    (4)authorizationCodeGrant：配置一些和Authorization Server交互有关的东西

*oauth2ResourceServer 想用这个功能需要加入spring-boot-starter-oauth2-resource-server
    (1)bearerTokenResolver:所谓BearerToken就是“Authorization:Bearer xxxxxxxx”，此处就要要配置一个从http请求里取出”xxxxxxxx“部分的Resolver类
    (2)accessDeniedHandler:在ExceptionTranslationFilter捕捉到错误后, 如果是AccessDeniedException类型的错误，就交给此处配置的AccessDeniedHandler
    (3)authenticationEntryPoint:在ExceptionTranslationFilter捕捉到错误后，会把请求和响应对象放入authenticationEntryPoint内处理。所谓处理，其实也就是为响应对象添加一些“未认证”、“无权限”之类的Header，或者直接把请求转发至登录界面
    (4)authenticationManagerResolver:AuthenticationManager负责完成认证动作，而AuthenticationManagerResolver的任务是从某对象中解析出一个AuthenticationManager
    (5)jwt:
    (6)opaqueToken:
为什么要配置这些？Resource Server可视为一个被spring security保护的、简单的web应用，当Client带着从Authorization Server拿到的token来它这里取数据的时候，Resource Server也需要执行一套认证流程，此处就用到了以上为它配置的一系列类


*requiresChannel
*httpBasic 可以设置三个属性：realmName,authenticationEntryPoint,authenticationDetailsSource
*requestMatchers

*requestMatcher
*antMatcher
*mvcMatcher
*regexMatcher
以上四个配置项都是用来设置HttpSecurity中一个名为requestMatcher的属性，四选一即可，否则后边的配置会覆盖前边的。而且前边在authorizeRequests里配置的路径也都会被覆盖。


httpFirewall spring security会把一些请求判断为危险请求，然后把这类请求包装为FirewalledRequest
debug
addSecurityFilterChainBuilder
privilegeEvaluator
expressionHandler
securityInterceptor
postBuildAction



在项目启动的时候，SpringBootWebSecurityConfiguration会自动把项目所有的端点都加上了保护

WebSecurityConfiguration类内定义了一个名为springSecurityFilterChain的bean，这个bean是一个Filter，会被加入到Servlet容器的Filter链中
所有对请求的拦截处理都是从这里开始的。

WebSecurity和HttpSecurity是对等的，两者都继承自AbstractConfiguredSecurityBuilder<Output, Builder>。它们的任务就是产出一个output类型的对象。
WebSecurity产出一个Filter，HttpSecurity产出一个DefaultSecurityFilterChain

先来看比较简单的WebSecurity
(1)从哪里来？
在WebSecurityConfiguration中有一个会自动执行的（？）setFilterChainProxySecurityConfigurer方法，此方法体内会new出一个WebSecurity对象来，这也是系统中唯一的一个WebSecurity。
(2)它经历了些什么？
系统内定义的所有SecurityConfigurer<Filter, WebSecurity>都会被spring框架归集在一起，作为一个list传到(1)中提到的那个方法内。这些configurer都需要一个WebSecurity对象作为参数才能正常工作，
此时正好将（1）中new出来的那个WebSecurity传给它们使用。而这些configurer正是我们自定义的那些配置类（其实通常只要有一个就足够了）。在这之后configurer们开始工作，然后产出了一个Filter，至此WebSecurity完成了它的使命。

再来看HttpSecurity
（1）同样地，它在哪里被创建出来？
有两个地方会产生HttpSecurity实例，一是HttpSecurityConfiguration内有一个名为httpSecurity的方法，spring会使用这个方法创建出HttpSecurity（注意这个方法是以prototype方式创建bean的）。
另一个地方是在WebSecurityConfigurerAdapter的getHttp方法内，使用直接new的方式创建创建出httpSecurity对象。
（2）被创建出来的HttpSecurity去了哪里？
在WebSecurityConfigurerAdapter的init方法内，httpSecurity被交给了系统中唯一的WebSecurity对象使用。这个httpSecurity为WebSecurity提供了两样东西，一个DefaultSecurityFilterChain和一个FilterSecurityInterceptor，然后就陷入了沉默。

总结一下，HttpSecurity被用户设置了一些配置信息，然后带着这些信息被送进了WebSecurity里。WebSecurity使用自身被配置的信息和HttpSecurity带来的配置信息，创建出一个Filter供servlet容器使用。

仅此而已



AuthenticationEntryPoint: 在ExceptionTranslationFilter捕捉到错误后，会把请求和响应对象放入authenticationEntryPoint内处理。所谓处理，其实也就是为响应对象添加一些“未认证”、“无权限”之类的Header，或者直接把请求转发至登录界面
OAuth2AuthorizedClient：资源的正真所有者（用户），自己作为Client在Authentication Server处的身份信息，Authentication Server针对用户给自己的token，这三个要素的集合在组成了一个“被认证的客户端”

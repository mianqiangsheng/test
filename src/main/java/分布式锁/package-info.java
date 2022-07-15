/**
 * 参考：https://mp.weixin.qq.com/s/0GKNLJlFdr8JwOkvAbwEFw
 *
 * 5.1 数据库分布式锁实现
 * 优点：
 * 简单，使用方便，不需要引入Redis、zookeeper等中间件。
 * 缺点：
 * 不适合高并发的场景
 * db操作性能较差；
 *
 * 5.2 Redis分布式锁实现
 * 优点：
 * 性能好，适合高并发场景
 * 较轻量级
 * 有较好的框架支持，如Redisson
 * 缺点：
 * 过期时间不好控制
 * 需要考虑锁被别的线程误删场景
 *
 * 5.3 Zookeeper分布式锁实现
 * 缺点：
 * 性能不如redis实现的分布式锁
 * 比较重的分布式锁。
 * 优点：
 * 有较好的性能和可靠性
 * 有封装较好的框架，如Curator
 *
 * 5.4 对比汇总
 * 从性能角度（从高到低）Redis > Zookeeper >= 数据库；
 * 从理解的难易程度角度（从低到高）数据库 > Redis > Zookeeper；
 * 从实现的复杂性角度（从低到高）Zookeeper > Redis > 数据库；
 * 从可靠性角度（从高到低）Zookeeper > Redis > 数据库。
 */
package 分布式锁;
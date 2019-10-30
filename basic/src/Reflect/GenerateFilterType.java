package Reflect;

/**
 * 根据ORM映射生成DQL 或 DML 脚本时的用到的筛选类型
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/30 10:35
 */
public enum GenerateFilterType {
    INSERT,
    UPDATE,
    SELECT,
    DELETE;
}

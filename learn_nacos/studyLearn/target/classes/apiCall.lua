--获取KEY
local key = KEYS[1]
--获取ARGV内的参数
local expire = tonumber(ARGV[1])
local count = tonumber(ARGV[2])

--获取key的次数
local current = redis.call('get', key)

--如果key的次数存在且大于预设值直接返回当前key的次数
if current and tonumber(current) > count then
    return tonumber(current);
end

--进行自增
current = redis.call('incr', key)
--获取key的过期时间
local ttl = redis.call('ttl', key)

--如果是第一次自增，设置过期时间
if tonumber(current) == 1 then
    redis.call('expire', key, expire)
else
    --如果key过期时间是永久，重新设置过期时间
    if ttl and tonumber(ttl) == -1 then
        redis.call('expire', key, expire)
    end
end

--返回key的次数
return tonumber(current)
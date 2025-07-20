```
| Algo                                | Accuracy    | Burst Support  | Memory     | Speed             | Use For                    |
| ----------------------------------- | ---------   | -------------  | ---------  | ---------------   | -------------------------- |
| Fixed Window Counter                | ❌ Low     | ❌ No          | ✅ Low     | ✅ Fast          | Simple APIs                |
| Sliding Log                         | ✅ High    | ✅ Yes         | ❌ High    | ❌ Slow          | Payments, audit logging    |
| Sliding Bucket Counter              | ⚠️ Medium  | ⚠️ Limited     | ✅ Bounded | ✅ Fast          | Dashboards, APIs           |
| Interpolated Sliding Counter        | ✅ High    | ⚠️ Some        | ✅ O(1)    | ✅ Fast          | Production APIs            |                            |
| Leaky Bucket                        | ✅ Good    | ❌ No          | ✅ O(1)    | ✅ Fast          | Video streaming, pipelines |
| Token Bucket                        | ✅ Best    | ✅ Yes         | ✅ O(1)    | ✅ Fast          | Modern API Gateways        |
```

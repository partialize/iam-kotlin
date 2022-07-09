[![build](https://github.com/partialize/iam/actions/workflows/build.yml/badge.svg)](https://github.com/partialize/iam/actions/workflows/build.yml)
[![check](https://github.com/partialize/iam/actions/workflows/check.yml/badge.svg)](https://github.com/partialize/iam/actions/workflows/check.yml)
[![k6-test](https://github.com/partialize/iam/actions/workflows/k6-test.yml/badge.svg)](https://github.com/partialize/iam/actions/workflows/k6-test.yml)
[![codecov](https://codecov.io/gh/partialize/spring-webflux-multi-module-boilerplate/branch/master/graph/badge.svg?token=ICZfrp7K5c)](https://codecov.io/gh/partialize/spring-webflux-multi-module-boilerplate)

# IAM
Identity and Access Management Service

## Bussiness Function
- User/client token issuance
- Scope(permission) management
- User/client Scope management
- Basic user/client CRUD
- Dynamic client based allowed origin header configuration

## Foundation Function
- In-memory cache that supports transactions
- Persistence management of domain objects like JPA
- Writing/reading converter auto configuration
- Easy configurate custom validation for basic validate annotion
- Support async event emitter
- RHS based filter and sort parser
- Easy conversion between dtos
- Update patch that distinguishes between undefined and null
- Easy addition of new types of authentication principal

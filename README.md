# EBICS Web Client

EBICS Web Client is a web UI which is used for exchanging of payments files via EBICS connection with the bank(s) supporting [EBICS protocol](https://www.ebics.de). 

### Demo application
Check the following [demo](https://ebics-web-client.herokuapp.com/) to get idea about functionality, here is the latest version of app deployed.

![Demo](demo.gif)

### Wiki pages
- [Installation manual](https://github.com/honza-toegel/ebics-java-client/wiki/Installation-Manual)
- [Getting started](https://github.com/honza-toegel/ebics-java-client/wiki/Getting-Started)

### Architecture & Functionality
![Architecture](ebics-web-client-architecture.drawio.png)

The web UI frontend uses REST API backend to execute EBICS orders. The core EBICS API is fork of the Java open source [EBICS java client](https://github.com/uwemaurer/ebics-java-client/) repository.
Thanks a lot for all contributions, otherwise this repo would not be here :-)

Main differences with this fork:

- Web UI (SPA based on Vue3 & Quasar), instead of console client
- REST API exposing EBICS operations
- Support of following EBICS versions 
  - EBICS 2.5 (H004)
  - EBICS 3.0 (H005)
- Added API for HTD & HEV
- Enhanced tracing of EBICS request/response operations, and all content
- Support of bcprov-jdk15on
- Some core API refactored to kotlin in order to increase readability, encapsulation and immutability and consistence
- Added UNIT Tests

#### Security & Authentication
The REST backend application use standard spring security, which is using only sample implementation of in-memory users and HTTP basic authentication. Is up to user to re-implement custom authentication if needed. The following three roles are differentiated:

| Role | Rights |
| --------------- | --------------- |
| ADMIN | Can administer banks & bank connections | 
| USER  | Can create & initialize own & shared bank connection and use them for uploading / downloading | 
| GUEST | Can use the shared "test" bank connections only - for uploading / downloading |

The EBICS user keys are persisted in the DB as java-key-store. User can use additionally the password (2FA) to protect access to his EBICS keys if desired. 

### Data model
The REST backend use simple JPA persistence layer. Access to core EBICS data is protected by Write/Read/UseAccessRightController interfaces which provides granular access control for single records in DB.

![Data Model](ebics-web-client-data-model.drawio.png)

### Ideas for roadmap

- Add sub-module (or separate repository) for E2E testing
- Add business content indexing in order to enable displaying of PSR status, HAC status in user-friendly way
- Add connection statistics support 
- Add possibility to sign the existing ebics order without signature, list orders waiting for signature
- Better UNIT Test coverage
- More business friendly Web UI for users which doesn't know anything about EBICS (like classical eBanking UI)
- Implementing UserDetailService to maintain user data  
- Download file indexing in order to find relevant data (like payment status by id, account statement by transaction,..) 
- Replace XMLBeans with JAXB
- Enhance re-usability of the submodules
  - Ensure that ebics-java-lib can be used standalone if needed for other use-cases 
  - Reduce tight coupling between ebics-dbmodel and ebics-rest-api
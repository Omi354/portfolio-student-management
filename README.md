## サービス概要

このプロジェクトは、IT技術を教えるスクールが受講生の情報を保持・分析するための管理システムです。  
受講生ではなくスクール側が使用することを想定し、CRUD機能をもつがログイン機能は持たないシンプルで使いやすいことを目指した実装内容となっています。

## 作成背景

JavaやSpring Bootの学習成果を形にするために作成しました。  
実務で頻繁に使用される以下の技術やツールを採用しています。

- REST APIの設計と実装: データのCRUD操作をサポート
- 自動テスト: JUnitを使用して単体テストを実装
- AWSを使用したデプロイ: クラウド環境へのアプリケーション展開

## 主な使用技術

### バックエンド

- Java: 21.0.5
- SpringBoot: 3.3.5

### インフラ・DB

- AWS: EC2, RDS, ALB
- MySQL: 8.0.40

### 使用ツール

- O/R Mapper: MyBatis
- 自動テスト: JUnit5
- CI/CDパイプライン: GitHub Actions

## 機能一覧

| 機能       | 詳細                                                             |
|:---------|:---------------------------------------------------------------|
| 受講生詳細の登録 | 氏名や居住地域などの受講生の情報と、受講コース・申込状況をセットで登録します                         |
| 受講生の条件検索 | 氏名・居住地域などの検索条件を指定し、条件に該当する受講生詳細を取得します　                         |
| 申込状況での検索 | 申込状況を指定し、該当する受講生詳細を取得します                                       |
| 受講生のID検索 | IDを指定し、一意の受講生詳細情報を取得します                                        |
| 受講生詳細の更新 | IDを指定し、任意の受講生情報を更新します<br/>※削除処理については論理削除として実装しているため、更新処理にて行います |
| 申込状況の更新  | IDを指定し、任意の申込状況を更新します。<br/>誤操作を防ぐため、状況が後ろに戻るような更新は本機能では実行できません  |

※ 言葉の定義は以下のとおりです

- 受講生： 氏名、居住地域、年齢などをもつオブジェクト
- 受講コース： コース名、開始日、終了日、申込状況などをもつオブジェクト
- 申込状況： 仮申込,本申込といった申込状況、作成日などをもつオブジェクト
- 受講生詳細： 受講生、受講コース（申込状況含む）をもつオブジェクト

## 設計書

### API仕様書

### ER図

```mermaid
erDiagram
    STUDENTS {
        varchar(36) id PK
        varchar(50) full_name
        varchar(100) kana
        varchar(50) nick_name
        varchar(254) email "UNIQUE"
        varchar(50) city
        int age
        enum gender "('Male', 'Female', 'NON_BINARY', 'Unspecified')"
        text remark
        tinyint is_deleted "0"
    }
    STUDENTS_COURSES {
        varchar(36) id PK
        varchar(36) student_id FK
        varchar(100) course_name
        timestamp start_date
        timestamp end_date
    }
    ENROLLMENT_STATUSES {
        varchar(36) id PK
        varchar(36) student_course_id FK
        enum status "('仮申込', '本申込', '受講中', '受講終了')"
        timestamp created_at "CURRENT_TIMESTAMP"
    }

    STUDENTS ||--o{ STUDENTS_COURSES: "enrolls"
    STUDENTS_COURSES ||--|| ENROLLMENT_STATUSES: "has"

```

### URL設計

| HTTP<br/>メソッド | URL                                 | 処理内容                 | 備考                                |
|---------------|-------------------------------------|----------------------|-----------------------------------|
| POST          | /students                           | 受講生詳細の作成             |                                   |
| GET           | /students                           | 受講生詳細の取得             | クエリパラメータを指定した場合は条件検索をします          |
| GET           | /students/{id}                      | IDに合致する<br/>受講生詳細の取得 |                                   |
| PUT           | /students                           | 受講生詳細の更新             |                                   |
| POST          | /students/courses/enrollment-status | 申込状況の更新              | 挙動としてはレコードをINSERTしますが、実質更新処理を行います |

### シーケンス図

```mermaid
sequenceDiagram
    actor User
    participant API as Spring Boot API
    participant DB as Database
    Note right of User: 受講生詳細の登録フロー
    User ->>+ API: POST /students (リクエストボディ：受講生詳細)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> API: UUIDなどのデフォルト値を設定
        API ->> DB: INSERT受講生
        API ->> DB: INSERT受講コース
        API ->> DB: INSERT申込状況
        API -->> User: 200 DBに登録した受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生の条件検索フロー
    User ->>+ API: GET /students (クエリパラメータ: 受講生フィールドの項目)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> DB: SELECT受講生
        DB -->> API: 受講生
        API -->> User: 200 受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 申込状況での検索フロー
    User ->>+ API: GET /students (クエリパラメータ: 申込状況)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> DB: SELECT受講コース（条件：申込状況）
        DB -->> API: 受講コース
        API ->> DB: SELECT受講生（条件：上記の受講コースに紐づく）
        DB -->> API: 受講生
        API -->> User: 200 受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生のID検索フロー
    User ->>+ API: GET /students/id
    API ->> API: IDの形式を検証
    alt IDの形式が正しい場合
        API ->> DB: SELECT受講生
        alt IDが存在する場合
            DB -->> API: 受講生
            API ->> DB: SELECT受講生コース
            DB -->> API: 受講生コース
            API -->> User: 200 受講生詳細
        else IDが存在しない場合
            DB -->> API: データなし
            API -->> User: 404 エラーメッセージ
        end
    else IDの形式が無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生詳細の更新フロー
    User ->>+ API: PUT /students（リクエストボディ：受講生詳細）
    API ->> API: 入力データ検証
    alt 入力データが正しい場合
        API ->> API: 入力データのIDを抽出
        API ->> DB: SELECT受講生(ID検索)
        API ->> DB: SELECT受講コース(ID検索)
        alt IDが存在する場合
            DB -->> API: データあり
            API ->> DB: UPDATE受講生
            API ->> DB: UPDATE受講コース
            API -->> User: 200 更新に成功しました
        else IDが存在しない場合
            DB -->> API: データなし
            API -->> User: 404 エラーメッセージ
        end
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 申込状況の更新フロー
    User ->>+ API: POST /students/courses/enrollment-status（リクエストボディ：申込状況）
    API ->> DB: SELECT申込状況（全件）
    DB -->> API: 申込状況（全件）
    API ->> API: リクエストボディの検証
    alt IDが存在する場合
        alt 申込状況が先に進むものの場合
            API ->> API: UUIDなどの初期値を設定
            API ->> DB: INSERT申込状況
            API -->> User: 200 更新に成功しました
        else 申込状況が後に戻るものの場合
            API -->> User: 400 エラーメッセージ
        end
    else IDが存在しない場合
        API -->> User: 404 エラーメッセージ
    end
```

### インフラ構成図

![](images/infrastructure-diagram.drawio.svg)

## テスト

## 自動テスト

## 作成スケジュール

## 工夫した点

- 実際のユースケースを想定した実装を行いました
  - 
    - 分析に使用できるよう、論理削除・UPDATEせずにINSERT処理
    - 申込状況更新について、後ろに戻るような修正を加える際に例外を発生させるよう実装しました
        - 該当箇所：
          - 

- 更新処理においてパフォーマンスを意識し、更新有無をチェックし、更新がある場合のみSQLを発行するように実装しました
    - 該当箇所：

## ハマった点

## 今後の展望
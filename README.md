# Webtoon 紹介サイト

Spring Boot と PostgreSQL を使用して Webtoon 紹介サイトを制作します。

## 解決したい部分

### ~~1. 画像ファイルが表示されない~~

Form でアップロードした画像ファイルが webtList.html, webtDetails.html に表示されません。
画像ファイルのパスを指定し、`static/images` フォルダ内に画像を格納する必要があります。

### ~~２．webtForm.htmlで入力した作者名が表示されない~~

Form で入力した作者名(author)が webtList.html, webtDetails.html に表示されません。

### ３．ジャンルでの検索機能が利かない

各作品のジャンルを検索機能で絞れません。

### ~~4. 複数のpostgreSQLのデータベースを接続できない~~

ログイン認証用のwebt_acctテーブルが接続出来ているか
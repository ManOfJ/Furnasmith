Furnasmith - Minecraft Mod
===============================

Version 1.8.9-1
![IMAGE](http://i.imgur.com/aNZ2q0F.gif "")

0. 既知の不具合
---------------

1. Mod 機能一覧
---------------

  - かまどでツールを焼くことで修理を可能にする
    - デフォルトでは二つクラフトして修理ができるツールが対象となります
  - エンチャントを保持したまま修理が可能
    - 逆にエンチャントを消去することも可能

2. インストール
---------------

  - 事前に [Minecraft Forge](http://files.minecraftforge.net/) をインストールしておいてください
  - ダウンロードした jar ファイルを Forge 環境の mods フォルダに配置してください
  - 以上です

3. コンフィグ
---------------

  - ゲーム内で変更する場合 ( 推奨 )
    - タイトル画面から Mods -> 一覧から Furnasmith を選択 -> Config ボタンを押下
    - ゲームメニュー ( ゲーム中 ESC ) の Mod Options... を選択 -> 一覧から Furnasmith を選択 -> Config  ボタンを押下
      - Note: 古いバージョンの Minecraft Forge だと動作しないことがあります
  - コンフィグファイルを直接編集する場合
    - Forge 環境コンフィグフォルダの Furnasmith.cfg をエディタで編集

修復可能･不可能指定ファイル

  - repairable.txt, blacklist.txt というファイルがそれぞれ修復可能, 修復不可能リストとして扱われます
    - 上記ファイルを Forge 環境コンフィグフォルダ配下の user フォルダ内に配置してください ( user フォルダは自動作成されないので手動で作成してください )
    - これらの機能を使用しないのであればファイル･フォルダを作成する必要はありません
    - repairable.txt はデフォルトでは修復ができないアイテムを修復したいときに使用します
  - ファイルに記述する情報の書式は以下の通りです
    - 書式1 ( クラス名指定 ) - 'CL クラス名'
    - 書式2 ( リソースロケーション指定 ) - 'RL リソースロケーション'
      - リソースロケーションとは 'ModID:アイテム名' で表される文字列

```
# blacklist.txt に記述することで 'すべての斧' と '金の剣' が修復対象外となる
CL net.minecraft.item.ItemAxe
RL minecraft:golden_sword
```

4. 今後の更新予定
---------------

5. 更新履歴
---------------

    Note:
    バージョンの後ろに表記されている Mdk のバージョンは開発に使用したものです
    このバージョンでなければ動作しない､ということはないのであくまで参考程度に考えてください

  - 1.8.9-1 @ Mdk 1.8.9-11.15.0.1694
    - Minecraft 1.8.9 に対応
  - 1.8-1 @ Mdk 1.8-11.14.4.1577
    - 公開

6. ライセンス
---------------

(c) Man of J, 2015-2016

この Mod は [Minecraft Mod Public License - Version 1.0.1](./LICENSE.md) のもとで提供されています

---------------

ご意見,ご要望,バグ報告などありましたら Issue か下記の連絡手段でお願いします
  - <man.of.j@outlook.com>
  - [Twitter: _ManOfJ](https://twitter.com/_ManOfJ)

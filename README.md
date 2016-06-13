Furnasmith - [Minecraft Mod][homepage]
===============================
Version: 1.9.4-2

![IMAGE](http://i.imgur.com/aNZ2q0F.gif "")


0. 既知の不具合
---------------


1. 今後の更新予定
-----------------


2. Mod 機能一覧
---------------

  - かまどでツールを焼くことで修理を可能にする
    - デフォルトでは二つクラフトして修理ができるツールが対象となります
  - エンチャントを保持したまま修理が可能
    - 逆にエンチャントを消去することも可能


3. インストール
---------------

  - 事前に [Minecraft Forge][forge] をインストールしておいてください
  - [DependencyResolver][resolver] をインストールしてください
  - [ダウンロード][homepage]した jar ファイルを mods フォルダに移動させます
  - 作業は以上です


4. 依存関係
-----------

    Note:  
      表記されている Minecraft Forge のバージョンは開発に使用したものです  
      このバージョンでなければ動作しない､ということはないのであくまで参考程度に考えてください

  - 1.9.4-2
    - [Minecraft Forge][forge]: 1.9.4-12.17.0.1940
    - [DependencyResolver][resolver]: 1.0
    - [MC-Commons][commons]:    1.9.4-0.0.1


  - 1.9.4-1
    - [Minecraft Forge][forge]: 1.9.4-12.17.0.1940
    - [MC-Commons][commons]:    1.9.4-0.0.1


  - 1.9-2
    - [Minecraft Forge][forge]: 1.9-12.16.1.1891


  - 1.9-1
    - [Minecraft Forge][forge]: 1.9-12.16.0.1767-1.9 ( ~ 1.9-12.16.0.1885 )


  - 1.8.9-1
    - [Minecraft Forge][forge]: 1.8.9-11.15.0.1694


  - 1.8-1
    - [Minecraft Forge][forge]: 1.8-11.14.4.1577


5. コンフィグ
-------------

  - 設定項目の詳細に関しては [Wiki](../../wiki/Configuration) を参照してください
  - ゲーム内で変更する場合 ( 推奨 )
    - タイトル画面から Mods -> 一覧から Furnasmith を選択 -> Config ボタンを押下
    - ゲームメニュー ( ゲーム中 ESC ) の Mod Options... を選択 -> 一覧から Furnasmith を選択 -> Config  ボタンを押下
      - Note: 古いバージョンの Minecraft Forge だと動作しないことがあります
  - コンフィグファイルを直接編集する場合
    - Forge 環境コンフィグフォルダの moj_fsmith.cfg をエディタで編集

修復可能･不可能指定ファイルについて

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


6. 更新履歴
-----------

  - 1.9.4-2
    - [DependencyResolver][resolver] での依存関係解決に対応


  - 1.9.4-1
    - Minecraft 1.9.4 に対応
    - 依存関係に [MC-Commons][commons] を追加


  - 1.9-2
    - Minecraft Forge 1.9-12.16.0.1886 以降に対応


  - 1.9-1
    - Minecraft 1.9 に対応
    - コンフィグのファイル名を Mod名.cfg から ModID.cfg に変更


  - 1.8.9-1
    - Minecraft 1.8.9 に対応


  - 1.8-1
    - 公開


7. ライセンス
-------------

(c) Man of J, 2015-2016

この Mod は [Minecraft Mod Public License - Version 1.0.1](./LICENSE.md) のもとで提供されています


--------------------------------

ご意見,ご要望,バグ報告などありましたら [Issues](../../issues) か下記の連絡手段でお願いします
  - E-mail: <man.of.j@outlook.com>
  - Twitter: [_ManOfJ](https://twitter.com/_ManOfJ)

--------------------------------

[//]: # ( リンクのエイリアス一覧 )

[homepage]: http://manofj.com/minecraft/
[forge]:    http://files.minecraftforge.net/
[resolver]: https://github.com/ManOfJ/DependencyResolver
[commons]:  https://github.com/ManOfJ/MC-Commons

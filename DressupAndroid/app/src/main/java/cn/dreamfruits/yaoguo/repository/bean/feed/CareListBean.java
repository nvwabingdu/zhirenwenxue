package cn.dreamfruits.yaoguo.repository.bean.feed;

import java.util.List;

/**
 * @Author qiwangi
 * @Date 2023/4/13
 * @TIME 15:23
 */

public class CareListBean {//用于伪数据测试
    //无参构造
    public CareListBean() {

    }

    /**
     * list
     */
    private List<ListDTO> list;
    /**
     * hasNext
     */
    private int hasNext;
    /**
     * lastTime
     */
    private int lastTime;

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }

    public static class ListDTO {
        /**
         * id
         */
        private int id;
        /**
         * title
         */
        private String title;
        /**
         * content
         */
        private String content;
        /**
         * type
         */
        private int type;
        /**
         * picUrls
         */
        private List<PicUrlsDTO> picUrls;
        /**
         * videoUrl
         */
        private List<VideoUrlDTO> videoUrl;
        /**
         * outfitId
         */
        private int outfitId;
        /**
         * atUser
         */
        private List<AtUserDTO> atUser;
        /**
         * config
         */
        private List<ConfigDTO> config;
        /**
         * provinceAdCode
         */
        private String provinceAdCode;
        /**
         * cityAdCode
         */
        private String cityAdCode;
        /**
         * address
         */
        private String address;
        /**
         * longitude
         */
        private int longitude;
        /**
         * latitude
         */
        private int latitude;
        /**
         * userInfo
         */
        private UserInfoDTO userInfo;
        /**
         * relation
         */
        private int relation;
        /**
         * laudCount
         */
        private int laudCount;
        /**
         * collectCount
         */
        private int collectCount;
        /**
         * commentCount
         */
        private int commentCount;
        /**
         * wearCount
         */
        private int wearCount;
        /**
         * isLaud
         */
        private int isLaud;
        /**
         * isCollect
         */
        private int isCollect;
        /**
         * state
         */
        private int state;
        /**
         * createTime
         */
        private int createTime;
        /**
         * updateTime
         */
        private int updateTime;
        /**
         * commentList
         */
        private List<CommentListDTO> commentList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<PicUrlsDTO> getPicUrls() {
            return picUrls;
        }

        public void setPicUrls(List<PicUrlsDTO> picUrls) {
            this.picUrls = picUrls;
        }

        public List<VideoUrlDTO> getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(List<VideoUrlDTO> videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getOutfitId() {
            return outfitId;
        }

        public void setOutfitId(int outfitId) {
            this.outfitId = outfitId;
        }

        public List<AtUserDTO> getAtUser() {
            return atUser;
        }

        public void setAtUser(List<AtUserDTO> atUser) {
            this.atUser = atUser;
        }

        public List<ConfigDTO> getConfig() {
            return config;
        }

        public void setConfig(List<ConfigDTO> config) {
            this.config = config;
        }

        public String getProvinceAdCode() {
            return provinceAdCode;
        }

        public void setProvinceAdCode(String provinceAdCode) {
            this.provinceAdCode = provinceAdCode;
        }

        public String getCityAdCode() {
            return cityAdCode;
        }

        public void setCityAdCode(String cityAdCode) {
            this.cityAdCode = cityAdCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getLongitude() {
            return longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public int getLatitude() {
            return latitude;
        }

        public void setLatitude(int latitude) {
            this.latitude = latitude;
        }

        public UserInfoDTO getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoDTO userInfo) {
            this.userInfo = userInfo;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public int getLaudCount() {
            return laudCount;
        }

        public void setLaudCount(int laudCount) {
            this.laudCount = laudCount;
        }

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getWearCount() {
            return wearCount;
        }

        public void setWearCount(int wearCount) {
            this.wearCount = wearCount;
        }

        public int getIsLaud() {
            return isLaud;
        }

        public void setIsLaud(int isLaud) {
            this.isLaud = isLaud;
        }

        public int getIsCollect() {
            return isCollect;
        }

        public void setIsCollect(int isCollect) {
            this.isCollect = isCollect;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public List<CommentListDTO> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListDTO> commentList) {
            this.commentList = commentList;
        }

        public static class UserInfoDTO {
            /**
             * id
             */
            private int id;
            /**
             * nickName
             */
            private String nickName;
            /**
             * avatarUrl
             */
            private String avatarUrl;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }
        }

        public static class PicUrlsDTO {
            /**
             * url
             */
            private String url;
            /**
             * width
             */
            private int width;
            /**
             * height
             */
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }

        public static class VideoUrlDTO {
            /**
             * url
             */
            private String url;
            /**
             * width
             */
            private int width;
            /**
             * height
             */
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }

        public static class AtUserDTO {
            /**
             * id
             */
            private int id;
            /**
             * name
             */
            private String name;
            /**
             * index
             */
            private int index;
            /**
             * len
             */
            private int len;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getLen() {
                return len;
            }

            public void setLen(int len) {
                this.len = len;
            }
        }

        public static class ConfigDTO {
            /**
             * id
             */
            private int id;
            /**
             * name
             */
            private String name;
            /**
             * index
             */
            private int index;
            /**
             * len
             */
            private int len;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getLen() {
                return len;
            }

            public void setLen(int len) {
                this.len = len;
            }
        }

        public static class CommentListDTO {
            /**
             * id
             */
            private int id;
            /**
             * targetId
             */
            private int targetId;
            /**
             * type
             */
            private int type;
            /**
             * content
             */
            private String content;
            /**
             * createTime
             */
            private int createTime;
            /**
             * uid
             */
            private int uid;
            /**
             * commentUserInfo
             */
            private CommentUserInfoDTO commentUserInfo;
            /**
             * replys
             */
            private Object replys;
            /**
             * replyCount
             */
            private int replyCount;
            /**
             * laudCount
             */
            private int laudCount;
            /**
             * isLaud
             */
            private int isLaud;
            /**
             * replyUid
             */
            private Object replyUid;
            /**
             * replyUserInfo
             */
            private Object replyUserInfo;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getTargetId() {
                return targetId;
            }

            public void setTargetId(int targetId) {
                this.targetId = targetId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public CommentUserInfoDTO getCommentUserInfo() {
                return commentUserInfo;
            }

            public void setCommentUserInfo(CommentUserInfoDTO commentUserInfo) {
                this.commentUserInfo = commentUserInfo;
            }

            public Object getReplys() {
                return replys;
            }

            public void setReplys(Object replys) {
                this.replys = replys;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }

            public int getLaudCount() {
                return laudCount;
            }

            public void setLaudCount(int laudCount) {
                this.laudCount = laudCount;
            }

            public int getIsLaud() {
                return isLaud;
            }

            public void setIsLaud(int isLaud) {
                this.isLaud = isLaud;
            }

            public Object getReplyUid() {
                return replyUid;
            }

            public void setReplyUid(Object replyUid) {
                this.replyUid = replyUid;
            }

            public Object getReplyUserInfo() {
                return replyUserInfo;
            }

            public void setReplyUserInfo(Object replyUserInfo) {
                this.replyUserInfo = replyUserInfo;
            }

            public static class CommentUserInfoDTO {
                /**
                 * id
                 */
                private int id;
                /**
                 * nickName
                 */
                private String nickName;
                /**
                 * avatarUrl
                 */
                private String avatarUrl;
                /**
                 * relation
                 */
                private int relation;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public String getAvatarUrl() {
                    return avatarUrl;
                }

                public void setAvatarUrl(String avatarUrl) {
                    this.avatarUrl = avatarUrl;
                }

                public int getRelation() {
                    return relation;
                }

                public void setRelation(int relation) {
                    this.relation = relation;
                }
            }
        }
    }
}

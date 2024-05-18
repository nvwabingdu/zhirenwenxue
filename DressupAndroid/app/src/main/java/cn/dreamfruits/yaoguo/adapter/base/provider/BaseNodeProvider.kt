package cn.dreamfruits.yaoguo.adapter.base.provider

import cn.dreamfruits.yaoguo.adapter.base.BaseNodeAdapter
import cn.dreamfruits.yaoguo.adapter.base.entity.node.BaseNode

abstract class BaseNodeProvider : BaseItemProvider<BaseNode>() {

    override fun getAdapter(): BaseNodeAdapter? {
        return super.getAdapter() as? BaseNodeAdapter
    }

}
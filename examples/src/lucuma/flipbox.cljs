(ns lucuma.flipbox
  (:require [lucuma.event :refer [fire]]
            [dommy.core :as dommy])
  (:require-macros [lucuma :refer [defwebcomponent]]
                   [dommy.macros :refer [sel1]]))

(def style
  "
lucu-flipbox{-webkit-box-sizing:border-box;-moz-box-sizing:border-box;box-sizing:border-box;display:block;position:relative;height:100%;width:100%;-webkit-transform-style:preserve-3d;-moz-transform-style:preserve-3d;-ms-transform-style:preserve-3d;-o-transform-style:preserve-3d;transform-style:preserve-3d}
xlucuflipbox > *{display:block;position:absolute;top:0;left:0;width:100%;height:100%;-webkit-backface-visibility:hidden;-moz-backface-visibility:hidden;-ms-backface-visibility:hidden;-o-backface-visibility:hidden;backface-visibility:hidden;-webkit-transition-property:-webkit-transform;-moz-transition-property:-moz-transform;-ms-transition-property:-ms-transform;-o-transition-property:-o-transform;transition-property:transform;-webkit-transition-duration:.25s;-moz-transition-duration:.25s;-ms-transition-duration:.25s;-o-transition-duration:.25s;transition-duration:.25s;-webkit-transition-timing-function:linear;-moz-transition-timing-function:linear;-ms-transition-timing-function:linear;-o-transition-timing-function:linear;transition-timing-function:linear;-webkit-transition-delay:0;-moz-transition-delay:0;-ms-transition-delay:0;-o-transition-delay:0;transition-delay:0;-webkit-transform-style:preserve-3d;-moz-transform-style:preserve-3d;-ms-transform-style:preserve-3d;-o-transform-style:preserve-3d;transform-style:preserve-3d}
lucu-flipbox > *:first-child{-webkit-transform:perspective(800px) rotateY(0) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateY(0) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateY(0) translate3d(0,0,2px);-o-transform:perspective(800px) rotateY(0) translate3d(0,0,2px);transform:perspective(800px) rotateY(0) translate3d(0,0,2px);z-index:2}
lucu-flipbox > *:last-child{-webkit-transform:perspective(800px) rotateY(180deg) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateY(180deg) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateY(180deg) translate3d(0,0,1px);-o-transform:perspective(800px) rotateY(180deg) translate3d(0,0,1px);transform:perspective(800px) rotateY(180deg) translate3d(0,0,1px);z-index:1}
lucu-flipbox[_anim-direction=\"up\"] > *:first-child,lucu-flipbox[_anim-direction=\"down\"] > *:first-child{-webkit-transform:perspective(800px) rotateX(0) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateX(0) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateX(0) translate3d(0,0,2px);-o-transform:perspective(800px) rotateX(0) translate3d(0,0,2px);transform:perspective(800px) rotateX(0) translate3d(0,0,2px)}
lucu-flipbox[_anim-direction=\"up\"] > *:last-child{-webkit-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,1px);-o-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,1px);transform:perspective(800px) rotateX(-180deg) translate3d(0,0,1px)}
lucu-flipbox[_anim-direction=\"down\"] > *:last-child{-webkit-transform:perspective(800px) rotateX(180deg) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateX(180deg) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateX(180deg) translate3d(0,0,1px);-o-transform:perspective(800px) rotateX(180deg) translate3d(0,0,1px);transform:perspective(800px) rotateX(180deg) translate3d(0,0,1px)}
lucu-flipbox[flipped]:after{content:\"\";display:none}
lucu-flipbox[flipped] > *:first-child{-webkit-transform:perspective(800px) rotateY(180deg) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateY(180deg) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateY(180deg) translate3d(0,0,2px);-o-transform:perspective(800px) rotateY(180deg) translate3d(0,0,2px);transform:perspective(800px) rotateY(180deg) translate3d(0,0,2px);z-index:1}
lucu-flipbox[flipped] > *:last-child{-webkit-transform:perspective(800px) rotateY(360deg) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateY(360deg) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateY(360deg) translate3d(0,0,1px);-o-transform:perspective(800px) rotateY(360deg) translate3d(0,0,1px);transform:perspective(800px) rotateY(360deg) translate3d(0,0,1px);z-index:2}
lucu-flipbox[_anim-direction=\"left\"][flipped] > *:first-child{-webkit-transform:perspective(800px) rotateY(-180deg) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateY(-180deg) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateY(-180deg) translate3d(0,0,2px);-o-transform:perspective(800px) rotateY(-180deg) translate3d(0,0,2px);transform:perspective(800px) rotateY(-180deg) translate3d(0,0,2px)}
lucu-flipbox[_anim-direction=\"left\"][flipped] > *:last-child{-webkit-transform:perspective(800px) rotateY(0) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateY(0) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateY(0) translate3d(0,0,1px);-o-transform:perspective(800px) rotateY(0) translate3d(0,0,1px);transform:perspective(800px) rotateY(0) translate3d(0,0,1px)}
lucu-flipbox[_anim-direction=\"up\"][flipped] > *:first-child{-webkit-transform:perspective(800px) rotateX(180deg) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateX(180deg) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateX(180deg) translate3d(0,0,2px);-o-transform:perspective(800px) rotateX(180deg) translate3d(0,0,2px);transform:perspective(800px) rotateX(180deg) translate3d(0,0,2px)}
lucu-flipbox[_anim-direction=\"up\"][flipped] > *:last-child{-webkit-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-o-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);transform:perspective(800px) rotateX(0) translate3d(0,0,1px)}
lucu-flipbox[_anim-direction=\"down\"][flipped] > *:first-child{-webkit-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,2px);-moz-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,2px);-ms-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,2px);-o-transform:perspective(800px) rotateX(-180deg) translate3d(0,0,2px);transform:perspective(800px) rotateX(-180deg) translate3d(0,0,2px)}
lucu-flipbox[_anim-direction=\"down\"][flipped] > *:last-child{-webkit-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-moz-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-ms-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);-o-transform:perspective(800px) rotateX(0) translate3d(0,0,1px);transform:perspective(800px) rotateX(0) translate3d(0,0,1px)}
  "
  )

(defn toggle
  [el]
  nil)

(defn show-front
  [el]
  nil)

(defn show-back
  [el]
  nil)

(defwebcomponent lucu-flipbox
  :content [:content]
  :style style
  :methods {:showFront show-front :showBack show-back :toggle toggle})

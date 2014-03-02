;
; Algebraic Geometry done according to Normal Wildberger's
;  Rational Trigonometry
;
(use `clojure.pprint)

;
; Miscellaneous stuff used in this file
;

(defn printd [obj]
  (binding [*print-suppress-namespaces* true] (pprint obj)))

;
; These functions could be replaced with implementations
; that define different fields.  We have a field that
; will start with integers, promote to rationals, and
; then promote to floats
;
(declare add)
(declare mul)
(declare div)
(declare sub)
(declare one)
(declare zero)
(declare minusone)

(defn minusone [] -1)
(defn one [] 1)
(defn zero [] 0)

;TODO: need to handle distributing multiplication with addition
(defn add [a b]
  (cond
    (= a (zero)) b
    (= b (zero)) a
    (and (number? a) (number? b)) (+ a b)
    :else [`add a b]))

(defn mul [a b]
  (cond
    (or (= a (zero)) (= b (zero))) (zero)
    (and (number? a) (number? b)) (* a b)
    :else [`mul a b]))

;This is basically a macro to define subtraction as addition
(defn sub [a b]
  (add a (mul (minusone) b)))

;Maybe refactor into inverse and multiply
(defn div [a b]
  (cond
    (= b (one)) a
    (and (number? a) (number? b)) (/ a b)
    :else [`div a b]))

;
; Now we can start to define geometry
;

(defn point [a b]
  [`point [`val a b]])

(defn line [a b c]
  [`line [`val a b c]])

;Given 2 points, compute the line through them
(defn lineFrom2Points [pa pb]
  (def va (get pa 1))
  (def vb (get pb 1))
  (def x (get va 1))
  (def x2 (get vb 1))
  (def y (get va 2))
  (def y2 (get vb 2))
  (def dx (sub x2 x))
  (def dy (sub y2 y))
  (def a (mul dy (minusone)))
  (def b dx)
  (def c (sub (mul dy x) (mul dx y)))
  (line a b c))

;Given 2 lines, compute the intersection
(defn intersectionPointFrom2Lines [l1 l2]
  (def ln1 (get l1 1))
  (def ln2 (get l2 1))
  (def a1 (get ln1 1))
  (def b1 (get ln1 2))
  (def c1 (get ln1 3))
  (def a2 (get ln2 1))
  (def b2 (get ln2 2))
  (def c2 (get ln2 3))
  (def xn (sub (mul b1 c2) (mul b2 c1))) 
  (def yn (sub (mul c1 a2) (mul c2 a1)))
  (def d (sub (mul a1 b2) (mul a2 b1)))
  (point (div xn d) (div yn d)))

;
; Define example problems to test the APIs 
;
 
(def pA (point 0 0))
(def pB (point 4 0))
(def pC (point 4 3))
(def lC (lineFrom2Points pA pB))
(def lB (lineFrom2Points pC pA))
(def lA (lineFrom2Points pB pC))

(printd lC)
(printd lB)
(printd lA)
(printd (intersectionPointFrom2Lines lA lB))


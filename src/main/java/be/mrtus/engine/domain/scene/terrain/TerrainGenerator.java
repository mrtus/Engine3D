package be.mrtus.engine.domain.scene.terrain;

import java.util.Arrays;
import java.util.concurrent.*;
import org.apache.commons.math3.util.FastMath;

public class TerrainGenerator {

	private static final int SMOOTH_RADIUS = 2;
	private final int[] emptyIndices = new int[]{0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7, 8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15, 16, 17, 18, 16, 18, 19, 20, 21, 22, 20, 22, 23, 24, 25, 26, 24, 26, 27, 1, 28, 29, 1, 29, 2, 5, 30, 31, 5, 31, 6, 9, 32, 33, 9, 33, 10, 13, 34, 35, 13, 35, 14, 17, 36, 37, 17, 37, 18, 21, 38, 39, 21, 39, 22, 25, 40, 41, 25, 41, 26, 28, 4, 7, 28, 7, 29, 30, 8, 11, 30, 11, 31, 32, 12, 15, 32, 15, 33, 34, 16, 19, 34, 19, 35, 36, 20, 23, 36, 23, 37, 38, 24, 27, 38, 27, 39, 33, 15, 42, 33, 42, 43, 35, 19, 44, 35, 44, 45, 37, 23, 46, 37, 46, 47, 39, 27, 48, 39, 48, 49, 3, 2, 50, 3, 50, 51, 7, 6, 52, 7, 52, 53, 11, 10, 54, 11, 54, 55, 15, 14, 56, 15, 56, 42, 19, 18, 57, 19, 57, 44, 23, 22, 58, 23, 58, 46, 27, 26, 59, 27, 59, 48, 2, 29, 60, 2, 60, 50, 6, 31, 61, 6, 61, 52, 10, 33, 43, 10, 43, 54, 14, 35, 45, 14, 45, 56, 18, 37, 47, 18, 47, 57, 22, 39, 49, 22, 49, 58, 26, 41, 62, 26, 62, 59, 29, 7, 53, 29, 53, 60, 31, 11, 55, 31, 55, 61, 49, 48, 63, 49, 63, 64, 51, 50, 65, 51, 65, 66, 53, 52, 67, 53, 67, 68, 55, 54, 69, 55, 69, 70, 42, 56, 71, 42, 71, 72, 44, 57, 73, 44, 73, 74, 46, 58, 75, 46, 75, 76, 48, 59, 77, 48, 77, 63, 50, 60, 78, 50, 78, 65, 52, 61, 79, 52, 79, 67, 54, 43, 80, 54, 80, 69, 56, 45, 81, 56, 81, 71, 57, 47, 82, 57, 82, 73, 58, 49, 64, 58, 64, 75, 59, 62, 83, 59, 83, 77, 60, 53, 68, 60, 68, 78, 61, 55, 70, 61, 70, 79, 43, 42, 72, 43, 72, 80, 45, 44, 74, 45, 74, 81, 47, 46, 76, 47, 76, 82, 66, 65, 84, 66, 84, 85, 68, 67, 86, 68, 86, 87, 70, 69, 88, 70, 88, 89, 72, 71, 90, 72, 90, 91, 74, 73, 92, 74, 92, 93, 76, 75, 94, 76, 94, 95, 63, 77, 96, 63, 96, 97, 65, 78, 98, 65, 98, 84, 67, 79, 99, 67, 99, 86, 69, 80, 100, 69, 100, 88, 71, 81, 101, 71, 101, 90, 73, 82, 102, 73, 102, 92, 75, 64, 103, 75, 103, 94, 77, 83, 104, 77, 104, 96, 78, 68, 87, 78, 87, 98, 79, 70, 89, 79, 89, 99, 80, 72, 91, 80, 91, 100, 81, 74, 93, 81, 93, 101, 82, 76, 95, 82, 95, 102, 64, 63, 97, 64, 97, 103, 85, 84, 105, 85, 105, 106, 87, 86, 107, 87, 107, 108, 89, 88, 109, 89, 109, 110, 91, 90, 111, 91, 111, 112, 93, 92, 113, 93, 113, 114, 95, 94, 115, 95, 115, 116, 97, 96, 117, 97, 117, 118, 84, 98, 119, 84, 119, 105, 86, 99, 120, 86, 120, 107, 88, 100, 121, 88, 121, 109, 90, 101, 122, 90, 122, 111, 92, 102, 123, 92, 123, 113, 94, 103, 124, 94, 124, 115, 96, 104, 125, 96, 125, 117, 98, 87, 108, 98, 108, 119, 99, 89, 110, 99, 110, 120, 100, 91, 112, 100, 112, 121, 101, 93, 114, 101, 114, 122, 102, 95, 116, 102, 116, 123, 103, 97, 118, 103, 118, 124, 110, 109, 126, 110, 126, 127, 112, 111, 128, 112, 128, 129, 114, 113, 130, 114, 130, 131, 116, 115, 132, 116, 132, 133, 118, 117, 134, 118, 134, 135, 105, 119, 136, 105, 136, 137, 107, 120, 138, 107, 138, 139, 109, 121, 140, 109, 140, 126, 111, 122, 141, 111, 141, 128, 113, 123, 142, 113, 142, 130, 115, 124, 143, 115, 143, 132, 117, 125, 144, 117, 144, 134, 119, 108, 145, 119, 145, 136, 120, 110, 127, 120, 127, 138, 121, 112, 129, 121, 129, 140, 122, 114, 131, 122, 131, 141, 123, 116, 133, 123, 133, 142, 124, 118, 135, 124, 135, 143, 106, 105, 137, 106, 137, 146, 108, 107, 139, 108, 139, 145, 131, 130, 147, 131, 147, 148, 133, 132, 149, 133, 149, 150, 135, 134, 151, 135, 151, 152, 137, 136, 153, 137, 153, 154, 139, 138, 155, 139, 155, 156, 126, 140, 157, 126, 157, 158, 128, 141, 159, 128, 159, 160, 130, 142, 161, 130, 161, 147, 132, 143, 162, 132, 162, 149, 134, 144, 163, 134, 163, 151, 136, 145, 164, 136, 164, 153, 138, 127, 165, 138, 165, 155, 140, 129, 166, 140, 166, 157, 141, 131, 148, 141, 148, 159, 142, 133, 150, 142, 150, 161, 143, 135, 152, 143, 152, 162, 146, 137, 154, 146, 154, 167, 145, 139, 156, 145, 156, 164, 127, 126, 158, 127, 158, 165, 129, 128, 160, 129, 160, 166, 154, 153, 168, 154, 168, 169, 156, 155, 170, 156, 170, 171, 158, 157, 172, 158, 172, 173, 160, 159, 174, 160, 174, 175, 147, 161, 176, 147, 176, 177, 149, 162, 178, 149, 178, 179, 151, 163, 180, 151, 180, 181, 153, 164, 182, 153, 182, 168, 155, 165, 183, 155, 183, 170, 157, 166, 184, 157, 184, 172, 159, 148, 185, 159, 185, 174, 161, 150, 186, 161, 186, 176, 162, 152, 187, 162, 187, 178, 167, 154, 169, 167, 169, 188, 164, 156, 171, 164, 171, 182, 165, 158, 173, 165, 173, 183, 166, 160, 175, 166, 175, 184, 148, 147, 177, 148, 177, 185, 150, 149, 179, 150, 179, 186, 152, 151, 181, 152, 181, 187, 169, 168, 189, 169, 189, 190, 171, 170, 191, 171, 191, 192, 173, 172, 193, 173, 193, 194, 175, 174, 195, 175, 195, 196, 177, 176, 197, 177, 197, 198, 179, 178, 199, 179, 199, 200, 181, 180, 201, 181, 201, 202, 168, 182, 203, 168, 203, 189, 170, 183, 204, 170, 204, 191, 172, 184, 205, 172, 205, 193, 174, 185, 206, 174, 206, 195, 176, 186, 207, 176, 207, 197, 178, 187, 208, 178, 208, 199, 188, 169, 190, 188, 190, 209, 182, 171, 192, 182, 192, 203, 183, 173, 194, 183, 194, 204, 184, 175, 196, 184, 196, 205, 185, 177, 198, 185, 198, 206, 186, 179, 200, 186, 200, 207, 187, 181, 202, 187, 202, 208, 190, 189, 210, 190, 210, 211, 192, 191, 212, 192, 212, 213, 194, 193, 214, 194, 214, 215, 196, 195, 216, 196, 216, 217, 198, 197, 218, 198, 218, 219, 200, 199, 220, 200, 220, 221, 202, 201, 222, 202, 222, 223, 189, 203, 224, 189, 224, 210, 191, 204, 225, 191, 225, 212, 193, 205, 226, 193, 226, 214, 195, 206, 227, 195, 227, 216, 197, 207, 228, 197, 228, 218, 199, 208, 229, 199, 229, 220, 209, 190, 211, 209, 211, 230, 203, 192, 213, 203, 213, 224, 204, 194, 215, 204, 215, 225, 205, 196, 217, 205, 217, 226, 206, 198, 219, 206, 219, 227, 207, 200, 221, 207, 221, 228, 208, 202, 223, 208, 223, 229, 217, 216, 231, 217, 231, 232, 219, 218, 233, 219, 233, 234, 221, 220, 235, 221, 235, 236, 223, 222, 237, 223, 237, 238, 210, 224, 239, 210, 239, 240, 212, 225, 241, 212, 241, 242, 214, 226, 243, 214, 243, 244, 216, 227, 245, 216, 245, 231, 218, 228, 246, 218, 246, 233, 220, 229, 247, 220, 247, 235, 230, 211, 248, 230, 248, 249, 224, 213, 250, 224, 250, 239, 225, 215, 251, 225, 251, 241, 226, 217, 232, 226, 232, 243, 227, 219, 234, 227, 234, 245, 228, 221, 236, 228, 236, 246, 229, 223, 238, 229, 238, 247, 211, 210, 240, 211, 240, 248, 213, 212, 242, 213, 242, 250, 215, 214, 244, 215, 244, 251, 236, 235, 252, 236, 252, 253, 238, 237, 254, 238, 254, 255, 240, 239, 256, 240, 256, 257, 242, 241, 258, 242, 258, 259, 244, 243, 260, 244, 260, 261, 231, 245, 262, 231, 262, 263, 233, 246, 264, 233, 264, 265, 235, 247, 266, 235, 266, 252, 249, 248, 267, 249, 267, 268, 239, 250, 269, 239, 269, 256, 241, 251, 270, 241, 270, 258, 243, 232, 271, 243, 271, 260, 245, 234, 272, 245, 272, 262, 246, 236, 253, 246, 253, 264, 247, 238, 255, 247, 255, 266, 248, 240, 257, 248, 257, 267, 250, 242, 259, 250, 259, 269, 251, 244, 261, 251, 261, 270, 232, 231, 263, 232, 263, 271, 234, 233, 265, 234, 265, 272, 257, 256, 273, 257, 273, 274, 259, 258, 275, 259, 275, 276, 261, 260, 277, 261, 277, 278, 263, 262, 279, 263, 279, 280, 265, 264, 281, 265, 281, 282, 252, 266, 283, 252, 283, 284, 268, 267, 285, 268, 285, 286, 256, 269, 287, 256, 287, 273, 258, 270, 288, 258, 288, 275, 260, 271, 289, 260, 289, 277, 262, 272, 290, 262, 290, 279, 264, 253, 291, 264, 291, 281, 266, 255, 292, 266, 292, 283, 267, 257, 274, 267, 274, 285, 269, 259, 276, 269, 276, 287, 270, 261, 278, 270, 278, 288, 271, 263, 280, 271, 280, 289, 272, 265, 282, 272, 282, 290, 253, 252, 284, 253, 284, 291, 255, 254, 293, 255, 293, 292, 274, 273, 294, 274, 294, 295, 276, 275, 296, 276, 296, 297, 278, 277, 298, 278, 298, 299, 280, 279, 300, 280, 300, 301, 282, 281, 302, 282, 302, 303, 284, 283, 304, 284, 304, 305, 286, 285, 306, 286, 306, 307, 273, 287, 308, 273, 308, 294, 275, 288, 309, 275, 309, 296, 277, 289, 310, 277, 310, 298, 279, 290, 311, 279, 311, 300, 281, 291, 312, 281, 312, 302, 283, 292, 313, 283, 313, 304, 285, 274, 295, 285, 295, 306, 287, 276, 297, 287, 297, 308, 288, 278, 299, 288, 299, 309, 289, 280, 301, 289, 301, 310, 290, 282, 303, 290, 303, 311, 291, 284, 305, 291, 305, 312, 292, 293, 314, 292, 314, 313, 297, 296, 315, 297, 315, 316, 299, 298, 317, 299, 317, 318, 301, 300, 319, 301, 319, 320, 303, 302, 321, 303, 321, 322, 305, 304, 323, 305, 323, 324, 307, 306, 325, 307, 325, 326, 294, 308, 327, 294, 327, 328, 296, 309, 329, 296, 329, 315, 298, 310, 330, 298, 330, 317, 300, 311, 331, 300, 331, 319, 302, 312, 332, 302, 332, 321, 304, 313, 333, 304, 333, 323, 306, 295, 334, 306, 334, 325, 308, 297, 316, 308, 316, 327, 309, 299, 318, 309, 318, 329, 310, 301, 320, 310, 320, 330, 311, 303, 322, 311, 322, 331, 312, 305, 324, 312, 324, 332, 313, 314, 335, 313, 335, 333, 295, 294, 328, 295, 328, 334, 322, 321, 336, 322, 336, 337, 324, 323, 338, 324, 338, 339, 326, 325, 340, 326, 340, 341, 328, 327, 342, 328, 342, 343, 315, 329, 344, 315, 344, 345, 317, 330, 346, 317, 346, 347, 319, 331, 348, 319, 348, 349, 321, 332, 350, 321, 350, 336, 323, 333, 351, 323, 351, 338, 325, 334, 352, 325, 352, 340, 327, 316, 353, 327, 353, 342, 329, 318, 354, 329, 354, 344, 330, 320, 355, 330, 355, 346, 331, 322, 337, 331, 337, 348, 332, 324, 339, 332, 339, 350, 333, 335, 356, 333, 356, 351, 334, 328, 343, 334, 343, 352, 316, 315, 345, 316, 345, 353, 318, 317, 347, 318, 347, 354, 320, 319, 349, 320, 349, 355, 341, 340, 357, 341, 357, 358, 343, 342, 359, 343, 359, 360, 345, 344, 361, 345, 361, 362, 347, 346, 363, 347, 363, 364, 349, 348, 365, 349, 365, 366, 336, 350, 367, 336, 367, 368, 338, 351, 369, 338, 369, 370, 340, 352, 371, 340, 371, 357, 342, 353, 372, 342, 372, 359, 344, 354, 373, 344, 373, 361, 346, 355, 374, 346, 374, 363, 348, 337, 375, 348, 375, 365, 350, 339, 376, 350, 376, 367, 351, 356, 377, 351, 377, 369, 352, 343, 360, 352, 360, 371, 353, 345, 362, 353, 362, 372, 354, 347, 364, 354, 364, 373, 355, 349, 366, 355, 366, 374, 337, 336, 368, 337, 368, 375, 339, 338, 370, 339, 370, 376, 358, 357, 378, 358, 378, 379, 360, 359, 380, 360, 380, 381, 362, 361, 382, 362, 382, 383, 364, 363, 384, 364, 384, 385, 366, 365, 386, 366, 386, 387, 368, 367, 388, 368, 388, 389, 370, 369, 390, 370, 390, 391, 357, 371, 392, 357, 392, 378, 359, 372, 393, 359, 393, 380, 361, 373, 394, 361, 394, 382, 363, 374, 395, 363, 395, 384, 365, 375, 396, 365, 396, 386, 367, 376, 397, 367, 397, 388, 369, 377, 398, 369, 398, 390, 371, 360, 381, 371, 381, 392, 372, 362, 383, 372, 383, 393, 373, 364, 385, 373, 385, 394, 374, 366, 387, 374, 387, 395, 375, 368, 389, 375, 389, 396, 376, 370, 391, 376, 391, 397, 381, 380, 399, 381, 399, 400, 383, 382, 401, 383, 401, 402, 385, 384, 403, 385, 403, 404, 387, 386, 405, 387, 405, 406, 389, 388, 407, 389, 407, 408, 391, 390, 409, 391, 409, 410, 378, 392, 411, 378, 411, 412, 380, 393, 413, 380, 413, 399, 382, 394, 414, 382, 414, 401, 384, 395, 415, 384, 415, 403, 386, 396, 416, 386, 416, 405, 388, 397, 417, 388, 417, 407, 390, 398, 418, 390, 418, 409, 392, 381, 400, 392, 400, 411, 393, 383, 402, 393, 402, 413, 394, 385, 404, 394, 404, 414, 395, 387, 406, 395, 406, 415, 396, 389, 408, 396, 408, 416, 397, 391, 410, 397, 410, 417, 379, 378, 412, 379, 412, 419, 404, 403, 420, 404, 420, 421, 406, 405, 422, 406, 422, 423, 408, 407, 424, 408, 424, 425, 410, 409, 426, 410, 426, 427, 412, 411, 428, 412, 428, 429, 399, 413, 430, 399, 430, 431, 401, 414, 432, 401, 432, 433, 403, 415, 434, 403, 434, 420, 405, 416, 435, 405, 435, 422, 407, 417, 436, 407, 436, 424, 409, 418, 437, 409, 437, 426, 411, 400, 438, 411, 438, 428, 413, 402, 439, 413, 439, 430, 414, 404, 421, 414, 421, 432, 415, 406, 423, 415, 423, 434, 416, 408, 425, 416, 425, 435, 417, 410, 427, 417, 427, 436, 419, 412, 429, 419, 429, 440, 400, 399, 431, 400, 431, 438, 402, 401, 433, 402, 433, 439};
	private final float[] emptyPositions = new float[]{0.0f, 0.0f, 40.0f, 2.0f, 0.0f, 40.0f, 2.0f, 0.0f, 38.0f, 0.0f, 0.0f, 38.0f, 6.0f, 0.0f, 40.0f, 8.0f, 0.0f, 40.0f, 8.0f, 0.0f, 38.0f, 6.0f, 0.0f, 38.0f, 12.0f, 0.0f, 40.0f, 14.0f, 0.0f, 40.0f, 14.0f, 0.0f, 38.0f, 12.0f, 0.0f, 38.0f, 18.0f, 0.0f, 40.0f, 20.0f, 0.0f, 40.0f, 20.0f, 0.0f, 38.0f, 18.0f, 0.0f, 38.0f, 24.0f, 0.0f, 40.0f, 26.0f, 0.0f, 40.0f, 26.0f, 0.0f, 38.0f, 24.0f, 0.0f, 38.0f, 30.0f, 0.0f, 40.0f, 32.0f, 0.0f, 40.0f, 32.0f, 0.0f, 38.0f, 30.0f, 0.0f, 38.0f, 36.0f, 0.0f, 40.0f, 38.0f, 0.0f, 40.0f, 38.0f, 0.0f, 38.0f, 36.0f, 0.0f, 38.0f, 4.0f, 0.0f, 40.0f, 4.0f, 0.0f, 38.0f, 10.0f, 0.0f, 40.0f, 10.0f, 0.0f, 38.0f, 16.0f, 0.0f, 40.0f, 16.0f, 0.0f, 38.0f, 22.0f, 0.0f, 40.0f, 22.0f, 0.0f, 38.0f, 28.0f, 0.0f, 40.0f, 28.0f, 0.0f, 38.0f, 34.0f, 0.0f, 40.0f, 34.0f, 0.0f, 38.0f, 40.0f, 0.0f, 40.0f, 40.0f, 0.0f, 38.0f, 18.0f, 0.0f, 36.0f, 16.0f, 0.0f, 36.0f, 24.0f, 0.0f, 36.0f, 22.0f, 0.0f, 36.0f, 30.0f, 0.0f, 36.0f, 28.0f, 0.0f, 36.0f, 36.0f, 0.0f, 36.0f, 34.0f, 0.0f, 36.0f, 2.0f, 0.0f, 36.0f, 0.0f, 0.0f, 36.0f, 8.0f, 0.0f, 36.0f, 6.0f, 0.0f, 36.0f, 14.0f, 0.0f, 36.0f, 12.0f, 0.0f, 36.0f, 20.0f, 0.0f, 36.0f, 26.0f, 0.0f, 36.0f, 32.0f, 0.0f, 36.0f, 38.0f, 0.0f, 36.0f, 4.0f, 0.0f, 36.0f, 10.0f, 0.0f, 36.0f, 40.0f, 0.0f, 36.0f, 36.0f, 0.0f, 34.0f, 34.0f, 0.0f, 34.0f, 2.0f, 0.0f, 34.0f, 0.0f, 0.0f, 34.0f, 8.0f, 0.0f, 34.0f, 6.0f, 0.0f, 34.0f, 14.0f, 0.0f, 34.0f, 12.0f, 0.0f, 34.0f, 20.0f, 0.0f, 34.0f, 18.0f, 0.0f, 34.0f, 26.0f, 0.0f, 34.0f, 24.0f, 0.0f, 34.0f, 32.0f, 0.0f, 34.0f, 30.0f, 0.0f, 34.0f, 38.0f, 0.0f, 34.0f, 4.0f, 0.0f, 34.0f, 10.0f, 0.0f, 34.0f, 16.0f, 0.0f, 34.0f, 22.0f, 0.0f, 34.0f, 28.0f, 0.0f, 34.0f, 40.0f, 0.0f, 34.0f, 2.0f, 0.0f, 32.0f, 0.0f, 0.0f, 32.0f, 8.0f, 0.0f, 32.0f, 6.0f, 0.0f, 32.0f, 14.0f, 0.0f, 32.0f, 12.0f, 0.0f, 32.0f, 20.0f, 0.0f, 32.0f, 18.0f, 0.0f, 32.0f, 26.0f, 0.0f, 32.0f, 24.0f, 0.0f, 32.0f, 32.0f, 0.0f, 32.0f, 30.0f, 0.0f, 32.0f, 38.0f, 0.0f, 32.0f, 36.0f, 0.0f, 32.0f, 4.0f, 0.0f, 32.0f, 10.0f, 0.0f, 32.0f, 16.0f, 0.0f, 32.0f, 22.0f, 0.0f, 32.0f, 28.0f, 0.0f, 32.0f, 34.0f, 0.0f, 32.0f, 40.0f, 0.0f, 32.0f, 2.0f, 0.0f, 30.0f, 0.0f, 0.0f, 30.0f, 8.0f, 0.0f, 30.0f, 6.0f, 0.0f, 30.0f, 14.0f, 0.0f, 30.0f, 12.0f, 0.0f, 30.0f, 20.0f, 0.0f, 30.0f, 18.0f, 0.0f, 30.0f, 26.0f, 0.0f, 30.0f, 24.0f, 0.0f, 30.0f, 32.0f, 0.0f, 30.0f, 30.0f, 0.0f, 30.0f, 38.0f, 0.0f, 30.0f, 36.0f, 0.0f, 30.0f, 4.0f, 0.0f, 30.0f, 10.0f, 0.0f, 30.0f, 16.0f, 0.0f, 30.0f, 22.0f, 0.0f, 30.0f, 28.0f, 0.0f, 30.0f, 34.0f, 0.0f, 30.0f, 40.0f, 0.0f, 30.0f, 14.0f, 0.0f, 28.0f, 12.0f, 0.0f, 28.0f, 20.0f, 0.0f, 28.0f, 18.0f, 0.0f, 28.0f, 26.0f, 0.0f, 28.0f, 24.0f, 0.0f, 28.0f, 32.0f, 0.0f, 28.0f, 30.0f, 0.0f, 28.0f, 38.0f, 0.0f, 28.0f, 36.0f, 0.0f, 28.0f, 4.0f, 0.0f, 28.0f, 2.0f, 0.0f, 28.0f, 10.0f, 0.0f, 28.0f, 8.0f, 0.0f, 28.0f, 16.0f, 0.0f, 28.0f, 22.0f, 0.0f, 28.0f, 28.0f, 0.0f, 28.0f, 34.0f, 0.0f, 28.0f, 40.0f, 0.0f, 28.0f, 6.0f, 0.0f, 28.0f, 0.0f, 0.0f, 28.0f, 26.0f, 0.0f, 26.0f, 24.0f, 0.0f, 26.0f, 32.0f, 0.0f, 26.0f, 30.0f, 0.0f, 26.0f, 38.0f, 0.0f, 26.0f, 36.0f, 0.0f, 26.0f, 4.0f, 0.0f, 26.0f, 2.0f, 0.0f, 26.0f, 10.0f, 0.0f, 26.0f, 8.0f, 0.0f, 26.0f, 16.0f, 0.0f, 26.0f, 14.0f, 0.0f, 26.0f, 22.0f, 0.0f, 26.0f, 20.0f, 0.0f, 26.0f, 28.0f, 0.0f, 26.0f, 34.0f, 0.0f, 26.0f, 40.0f, 0.0f, 26.0f, 6.0f, 0.0f, 26.0f, 12.0f, 0.0f, 26.0f, 18.0f, 0.0f, 26.0f, 0.0f, 0.0f, 26.0f, 4.0f, 0.0f, 24.0f, 2.0f, 0.0f, 24.0f, 10.0f, 0.0f, 24.0f, 8.0f, 0.0f, 24.0f, 16.0f, 0.0f, 24.0f, 14.0f, 0.0f, 24.0f, 22.0f, 0.0f, 24.0f, 20.0f, 0.0f, 24.0f, 28.0f, 0.0f, 24.0f, 26.0f, 0.0f, 24.0f, 34.0f, 0.0f, 24.0f, 32.0f, 0.0f, 24.0f, 40.0f, 0.0f, 24.0f, 38.0f, 0.0f, 24.0f, 6.0f, 0.0f, 24.0f, 12.0f, 0.0f, 24.0f, 18.0f, 0.0f, 24.0f, 24.0f, 0.0f, 24.0f, 30.0f, 0.0f, 24.0f, 36.0f, 0.0f, 24.0f, 0.0f, 0.0f, 24.0f, 4.0f, 0.0f, 22.0f, 2.0f, 0.0f, 22.0f, 10.0f, 0.0f, 22.0f, 8.0f, 0.0f, 22.0f, 16.0f, 0.0f, 22.0f, 14.0f, 0.0f, 22.0f, 22.0f, 0.0f, 22.0f, 20.0f, 0.0f, 22.0f, 28.0f, 0.0f, 22.0f, 26.0f, 0.0f, 22.0f, 34.0f, 0.0f, 22.0f, 32.0f, 0.0f, 22.0f, 40.0f, 0.0f, 22.0f, 38.0f, 0.0f, 22.0f, 6.0f, 0.0f, 22.0f, 12.0f, 0.0f, 22.0f, 18.0f, 0.0f, 22.0f, 24.0f, 0.0f, 22.0f, 30.0f, 0.0f, 22.0f, 36.0f, 0.0f, 22.0f, 0.0f, 0.0f, 22.0f, 4.0f, 0.0f, 20.0f, 2.0f, 0.0f, 20.0f, 10.0f, 0.0f, 20.0f, 8.0f, 0.0f, 20.0f, 16.0f, 0.0f, 20.0f, 14.0f, 0.0f, 20.0f, 22.0f, 0.0f, 20.0f, 20.0f, 0.0f, 20.0f, 28.0f, 0.0f, 20.0f, 26.0f, 0.0f, 20.0f, 34.0f, 0.0f, 20.0f, 32.0f, 0.0f, 20.0f, 40.0f, 0.0f, 20.0f, 38.0f, 0.0f, 20.0f, 6.0f, 0.0f, 20.0f, 12.0f, 0.0f, 20.0f, 18.0f, 0.0f, 20.0f, 24.0f, 0.0f, 20.0f, 30.0f, 0.0f, 20.0f, 36.0f, 0.0f, 20.0f, 0.0f, 0.0f, 20.0f, 22.0f, 0.0f, 18.0f, 20.0f, 0.0f, 18.0f, 28.0f, 0.0f, 18.0f, 26.0f, 0.0f, 18.0f, 34.0f, 0.0f, 18.0f, 32.0f, 0.0f, 18.0f, 40.0f, 0.0f, 18.0f, 38.0f, 0.0f, 18.0f, 6.0f, 0.0f, 18.0f, 4.0f, 0.0f, 18.0f, 12.0f, 0.0f, 18.0f, 10.0f, 0.0f, 18.0f, 18.0f, 0.0f, 18.0f, 16.0f, 0.0f, 18.0f, 24.0f, 0.0f, 18.0f, 30.0f, 0.0f, 18.0f, 36.0f, 0.0f, 18.0f, 2.0f, 0.0f, 18.0f, 0.0f, 0.0f, 18.0f, 8.0f, 0.0f, 18.0f, 14.0f, 0.0f, 18.0f, 34.0f, 0.0f, 16.0f, 32.0f, 0.0f, 16.0f, 40.0f, 0.0f, 16.0f, 38.0f, 0.0f, 16.0f, 6.0f, 0.0f, 16.0f, 4.0f, 0.0f, 16.0f, 12.0f, 0.0f, 16.0f, 10.0f, 0.0f, 16.0f, 18.0f, 0.0f, 16.0f, 16.0f, 0.0f, 16.0f, 24.0f, 0.0f, 16.0f, 22.0f, 0.0f, 16.0f, 30.0f, 0.0f, 16.0f, 28.0f, 0.0f, 16.0f, 36.0f, 0.0f, 16.0f, 2.0f, 0.0f, 16.0f, 0.0f, 0.0f, 16.0f, 8.0f, 0.0f, 16.0f, 14.0f, 0.0f, 16.0f, 20.0f, 0.0f, 16.0f, 26.0f, 0.0f, 16.0f, 6.0f, 0.0f, 14.0f, 4.0f, 0.0f, 14.0f, 12.0f, 0.0f, 14.0f, 10.0f, 0.0f, 14.0f, 18.0f, 0.0f, 14.0f, 16.0f, 0.0f, 14.0f, 24.0f, 0.0f, 14.0f, 22.0f, 0.0f, 14.0f, 30.0f, 0.0f, 14.0f, 28.0f, 0.0f, 14.0f, 36.0f, 0.0f, 14.0f, 34.0f, 0.0f, 14.0f, 2.0f, 0.0f, 14.0f, 0.0f, 0.0f, 14.0f, 8.0f, 0.0f, 14.0f, 14.0f, 0.0f, 14.0f, 20.0f, 0.0f, 14.0f, 26.0f, 0.0f, 14.0f, 32.0f, 0.0f, 14.0f, 38.0f, 0.0f, 14.0f, 40.0f, 0.0f, 14.0f, 6.0f, 0.0f, 12.0f, 4.0f, 0.0f, 12.0f, 12.0f, 0.0f, 12.0f, 10.0f, 0.0f, 12.0f, 18.0f, 0.0f, 12.0f, 16.0f, 0.0f, 12.0f, 24.0f, 0.0f, 12.0f, 22.0f, 0.0f, 12.0f, 30.0f, 0.0f, 12.0f, 28.0f, 0.0f, 12.0f, 36.0f, 0.0f, 12.0f, 34.0f, 0.0f, 12.0f, 2.0f, 0.0f, 12.0f, 0.0f, 0.0f, 12.0f, 8.0f, 0.0f, 12.0f, 14.0f, 0.0f, 12.0f, 20.0f, 0.0f, 12.0f, 26.0f, 0.0f, 12.0f, 32.0f, 0.0f, 12.0f, 38.0f, 0.0f, 12.0f, 40.0f, 0.0f, 12.0f, 12.0f, 0.0f, 10.0f, 10.0f, 0.0f, 10.0f, 18.0f, 0.0f, 10.0f, 16.0f, 0.0f, 10.0f, 24.0f, 0.0f, 10.0f, 22.0f, 0.0f, 10.0f, 30.0f, 0.0f, 10.0f, 28.0f, 0.0f, 10.0f, 36.0f, 0.0f, 10.0f, 34.0f, 0.0f, 10.0f, 2.0f, 0.0f, 10.0f, 0.0f, 0.0f, 10.0f, 8.0f, 0.0f, 10.0f, 6.0f, 0.0f, 10.0f, 14.0f, 0.0f, 10.0f, 20.0f, 0.0f, 10.0f, 26.0f, 0.0f, 10.0f, 32.0f, 0.0f, 10.0f, 38.0f, 0.0f, 10.0f, 4.0f, 0.0f, 10.0f, 40.0f, 0.0f, 10.0f, 30.0f, 0.0f, 8.0f, 28.0f, 0.0f, 8.0f, 36.0f, 0.0f, 8.0f, 34.0f, 0.0f, 8.0f, 2.0f, 0.0f, 8.0f, 0.0f, 0.0f, 8.0f, 8.0f, 0.0f, 8.0f, 6.0f, 0.0f, 8.0f, 14.0f, 0.0f, 8.0f, 12.0f, 0.0f, 8.0f, 20.0f, 0.0f, 8.0f, 18.0f, 0.0f, 8.0f, 26.0f, 0.0f, 8.0f, 24.0f, 0.0f, 8.0f, 32.0f, 0.0f, 8.0f, 38.0f, 0.0f, 8.0f, 4.0f, 0.0f, 8.0f, 10.0f, 0.0f, 8.0f, 16.0f, 0.0f, 8.0f, 22.0f, 0.0f, 8.0f, 40.0f, 0.0f, 8.0f, 2.0f, 0.0f, 6.0f, 0.0f, 0.0f, 6.0f, 8.0f, 0.0f, 6.0f, 6.0f, 0.0f, 6.0f, 14.0f, 0.0f, 6.0f, 12.0f, 0.0f, 6.0f, 20.0f, 0.0f, 6.0f, 18.0f, 0.0f, 6.0f, 26.0f, 0.0f, 6.0f, 24.0f, 0.0f, 6.0f, 32.0f, 0.0f, 6.0f, 30.0f, 0.0f, 6.0f, 38.0f, 0.0f, 6.0f, 36.0f, 0.0f, 6.0f, 4.0f, 0.0f, 6.0f, 10.0f, 0.0f, 6.0f, 16.0f, 0.0f, 6.0f, 22.0f, 0.0f, 6.0f, 28.0f, 0.0f, 6.0f, 34.0f, 0.0f, 6.0f, 40.0f, 0.0f, 6.0f, 2.0f, 0.0f, 4.0f, 0.0f, 0.0f, 4.0f, 8.0f, 0.0f, 4.0f, 6.0f, 0.0f, 4.0f, 14.0f, 0.0f, 4.0f, 12.0f, 0.0f, 4.0f, 20.0f, 0.0f, 4.0f, 18.0f, 0.0f, 4.0f, 26.0f, 0.0f, 4.0f, 24.0f, 0.0f, 4.0f, 32.0f, 0.0f, 4.0f, 30.0f, 0.0f, 4.0f, 38.0f, 0.0f, 4.0f, 36.0f, 0.0f, 4.0f, 4.0f, 0.0f, 4.0f, 10.0f, 0.0f, 4.0f, 16.0f, 0.0f, 4.0f, 22.0f, 0.0f, 4.0f, 28.0f, 0.0f, 4.0f, 34.0f, 0.0f, 4.0f, 40.0f, 0.0f, 4.0f, 8.0f, 0.0f, 2.0f, 6.0f, 0.0f, 2.0f, 14.0f, 0.0f, 2.0f, 12.0f, 0.0f, 2.0f, 20.0f, 0.0f, 2.0f, 18.0f, 0.0f, 2.0f, 26.0f, 0.0f, 2.0f, 24.0f, 0.0f, 2.0f, 32.0f, 0.0f, 2.0f, 30.0f, 0.0f, 2.0f, 38.0f, 0.0f, 2.0f, 36.0f, 0.0f, 2.0f, 4.0f, 0.0f, 2.0f, 2.0f, 0.0f, 2.0f, 10.0f, 0.0f, 2.0f, 16.0f, 0.0f, 2.0f, 22.0f, 0.0f, 2.0f, 28.0f, 0.0f, 2.0f, 34.0f, 0.0f, 2.0f, 40.0f, 0.0f, 2.0f, 0.0f, 0.0f, 2.0f, 20.0f, 0.0f, 0.0f, 18.0f, 0.0f, 0.0f, 26.0f, 0.0f, 0.0f, 24.0f, 0.0f, 0.0f, 32.0f, 0.0f, 0.0f, 30.0f, 0.0f, 0.0f, 38.0f, 0.0f, 0.0f, 36.0f, 0.0f, 0.0f, 4.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 10.0f, 0.0f, 0.0f, 8.0f, 0.0f, 0.0f, 16.0f, 0.0f, 0.0f, 14.0f, 0.0f, 0.0f, 22.0f, 0.0f, 0.0f, 28.0f, 0.0f, 0.0f, 34.0f, 0.0f, 0.0f, 40.0f, 0.0f, 0.0f, 6.0f, 0.0f, 0.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
	private final ExecutorService execService = Executors.newFixedThreadPool(5);
	private int seed;

	public TerrainGenerator(String seed) {
		this.setSeed(seed);
	}

	public float calculateHeight(int x, int y) {
		int help = this.noise(this.seed, x, y);
		if(help < 64) {
			help += 128;
		}
		return help - 90;
//		return (float)(help / 1.0) - (32 + 64 + 8);
	}

	public void destroy() {
		this.execService.shutdownNow();
	}

	public void generateTerrain(TerrainChunk chunk) {
		this.execService.submit(() -> {
			try {
				this.generateTerrainChunk(chunk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void setSeed(String seed) {
		this.seed = seed.hashCode();
	}

	private float[][] calculateHeights(TerrainChunk chunk) {
		float[][] heightMap = new float[TerrainChunk.SIZE + SMOOTH_RADIUS * 2][TerrainChunk.SIZE + SMOOTH_RADIUS * 2];
		for (int x = -SMOOTH_RADIUS; x < TerrainChunk.SIZE + SMOOTH_RADIUS; x++) {
			for (int y = -SMOOTH_RADIUS; y < TerrainChunk.SIZE + SMOOTH_RADIUS; y++) {
				int xReal = chunk.getPosition().x + x;
				int yReal = chunk.getPosition().y + y;
				heightMap[x + SMOOTH_RADIUS][y + SMOOTH_RADIUS] = this.calculateHeight(xReal, yReal);
			}
		}
//		String line = "";
//		for (int x = 0; x < heightMap.length; x++) {
//			for (int y = 0; y < heightMap.length; y++) {
//				line += heightMap[x][y] + " ";
//			}
//			line += "\n";
//		}
//		line += "=====s";
//		System.out.println(line);
		return heightMap;
	}

	private float[][] calculateSmoothHeights(float[][] heightMap) {
		float[][] smoothHeightMap = new float[TerrainChunk.SIZE][TerrainChunk.SIZE];
		for (int x = SMOOTH_RADIUS; x < TerrainChunk.SIZE + SMOOTH_RADIUS; x++) {
			for (int y = SMOOTH_RADIUS; y < TerrainChunk.SIZE + SMOOTH_RADIUS; y++) {
				float smoothHeight = (heightMap[x - SMOOTH_RADIUS][y - SMOOTH_RADIUS]
						+ heightMap[x - SMOOTH_RADIUS][y + SMOOTH_RADIUS]
						+ heightMap[x + SMOOTH_RADIUS][y - SMOOTH_RADIUS]
						+ heightMap[x + SMOOTH_RADIUS][y + SMOOTH_RADIUS]
						+ heightMap[x - SMOOTH_RADIUS][y]
						+ heightMap[x + SMOOTH_RADIUS][y]
						+ heightMap[x][y - SMOOTH_RADIUS]
						+ heightMap[x][y + SMOOTH_RADIUS])
						/ 8;
				smoothHeightMap[x - SMOOTH_RADIUS][y - SMOOTH_RADIUS] = smoothHeight;
			}
		}
		return smoothHeightMap;
	}

	private void generateTerrainChunk(TerrainChunk chunk) {
		float[][] smoothHeightMap = this.calculateSmoothHeights(this.calculateHeights(chunk));
		TerrainModel model = this.generateTerrainModel(smoothHeightMap);
		chunk.setModel(model);
		chunk.setHeightMap(smoothHeightMap);
	}

	private TerrainModel generateTerrainModel(float[][] heightMap) {
		float[] arr = Arrays.copyOf(this.emptyPositions, this.emptyPositions.length);
		for (int i = 0; i < arr.length; i += 3) {
			arr[i + 1] = heightMap[(int)arr[i]][(int)FastMath.abs(arr[i + 2])];
		}
		TerrainModel model = new TerrainModel.TerrainModelBuilder()
				.setPositions(arr, this.emptyIndices)
				.build();
		return model;
	}

	private int noise(int seed, double x, double z) {
		int nOctave = 200;
		int result = 0;
		float frequence256 = 0.5f; // wider landscape ...
		int sx = (int)((x) * frequence256);
		int sy = (int)((z) * frequence256);
		int octave = nOctave;
		while (octave != 0) {
			int bX = sx & 0xFF;
			int bY = sy & 0xFF;
			int sxp = sx >> 8;
			int syp = sy >> 8;

			// Compute noise for each corner of current cell
			int Y1376312589_00 = syp * seed;
			int Y1376312589_01 = Y1376312589_00 + seed;

			int XY1376312589_00 = sxp + Y1376312589_00;
			int XY1376312589_10 = XY1376312589_00 + 1;
			int XY1376312589_01 = sxp + Y1376312589_01;
			int XY1376312589_11 = XY1376312589_01 + 1;

			int XYBASE_00 = (XY1376312589_00 << 13) ^ XY1376312589_00;
			int XYBASE_10 = (XY1376312589_10 << 13) ^ XY1376312589_10;
			int XYBASE_01 = (XY1376312589_01 << 13) ^ XY1376312589_01;
			int XYBASE_11 = (XY1376312589_11 << 13) ^ XY1376312589_11;

			int alt1 = (XYBASE_00 * (XYBASE_00 * XYBASE_00 * 15731 + 789221) + seed);
			int alt2 = (XYBASE_10 * (XYBASE_10 * XYBASE_10 * 15731 + 789221) + seed);
			int alt3 = (XYBASE_01 * (XYBASE_01 * XYBASE_01 * 15731 + 789221) + seed);
			int alt4 = (XYBASE_11 * (XYBASE_11 * XYBASE_11 * 15731 + 789221) + seed);

			/*
			 NOTE : on for true grandiant noise uncomment following block for
			 true gradiant we need to perform scalar product here, gradiant
			 vector are created/deducted using the above pseudo random values
			 (alt1...alt4) : by cutting thoses values in twice values to get
			 for each a fixed x,y vector gradX1= alt1&0xFF gradY1=
			 (alt1&0xFF00)>>8

			 the last part of the PRN (alt1&0xFF0000)>>8 is used as an offset
			 to correct one of the gradiant problem wich is zero on cell edge

			 source vector (sXN;sYN) for scalar product are computed using
			 (bX,bY)

			 each four values must be replaced by the result of the following
			 altN=(gradXN;gradYN) scalar (sXN;sYN)

			 all the rest of the code (interpolation+accumulation) is
			 identical for value & gradiant noise
			 */
			/*
			 START BLOCK FOR TRUE GRADIANT NOISE
			 */
			int grad1X = (alt1 & 0xFF) - 128;
			int grad1Y = ((alt1 >> 8) & 0xFF) - 128;
			int grad2X = (alt2 & 0xFF) - 128;
			int grad2Y = ((alt2 >> 8) & 0xFF) - 128;
			int grad3X = (alt3 & 0xFF) - 128;
			int grad3Y = ((alt3 >> 8) & 0xFF) - 128;
			int grad4X = (alt4 & 0xFF) - 128;
			int grad4Y = ((alt4 >> 8) & 0xFF) - 128;

			int sX1 = bX >> 1;
			int sY1 = bY >> 1;
			int sX2 = 128 - sX1;
			int sY2 = sY1;
			int sX3 = sX1;
			int sY3 = 128 - sY1;
			int sX4 = 128 - sX1;
			int sY4 = 128 - sY1;
			alt1 = (grad1X * sX1 + grad1Y * sY1) + 16384 + ((alt1 & 0xFF0000) >> 9);
			// to avoid seams to be 0 we use an offset
			alt2 = (grad2X * sX2 + grad2Y * sY2) + 16384 + ((alt2 & 0xFF0000) >> 9);
			alt3 = (grad3X * sX3 + grad3Y * sY3) + 16384 + ((alt3 & 0xFF0000) >> 9);
			alt4 = (grad4X * sX4 + grad4Y * sY4) + 16384 + ((alt4 & 0xFF0000) >> 9);

			/*
			 END BLOCK FOR TRUE GRADIANT NOISE
			 */
			/*
			 START BLOCK FOR VALUE NOISE
			 */
//			alt1 &= 0xFFFF;
//			alt2 &= 0xFFFF;
//			alt3 &= 0xFFFF;
//			alt4 &= 0xFFFF;

			/*
			 END BLOCK FOR VALUE NOISE
			 */
			/*
			 START BLOCK FOR LINEAR INTERPOLATION
			 */
			// BiLinear interpolation
			// int f24 = (bX * bY) >> 8;
			// int f23 = bX - f24;
			// int f14 = bY - f24;
			// int f13 = 256 - f14 - f23 - f24;
			//
			// int val = (alt1 * f13 + alt2 * f23 + alt3 * f14 + alt4 * f24);

			/*
			 END BLOCK FOR LINEAR INTERPOLATION
			 */
			// BiCubic interpolation ( in the form alt(bX) = alt[n] - (3*bX^2 -
			// 2*bX^3) * (alt[n] - alt[n+1]) )
			/*
			 START BLOCK FOR BICUBIC INTERPOLATION
			 */
			int bX2 = (bX * bX) >> 8;
			int bX3 = (bX2 * bX) >> 8;
			int _3bX2 = 3 * bX2;
			int _2bX3 = 2 * bX3;
			int alt12 = alt1 - (((_3bX2 - _2bX3) * (alt1 - alt2)) >> 8);
			int alt34 = alt3 - (((_3bX2 - _2bX3) * (alt3 - alt4)) >> 8);

			int bY2 = (bY * bY) >> 8;
			int bY3 = (bY2 * bY) >> 8;
			int _3bY2 = 3 * bY2;
			int _2bY3 = 2 * bY3;
			int val = alt12 - (((_3bY2 - _2bY3) * (alt12 - alt34)) >> 8);

			val *= 256;

			/*
			 END BLOCK FOR BICUBIC INTERPOLATION
			 */
			// Accumulate in result
			result += (val << octave);

			octave--;
			sx <<= 1;
			sy <<= 1;
		}
		return result >>> 25; // 200 = nOctave
	}
}
